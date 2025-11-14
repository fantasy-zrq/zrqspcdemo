package com.example.model.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.common.context.UserContext;
import com.example.model.common.exception.ClientException;
import com.example.model.dto.req.TaskCreateReqDTO;
import com.example.model.entity.CouponDO;
import com.example.model.entity.TaskDO;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.entity.mapper.TaskMapper;
import com.example.model.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author zrq
 */
@RequiredArgsConstructor
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskDO> implements TaskService {

    private final CouponMapper couponMapper;
    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public void createTask(TaskCreateReqDTO requestParam) {
        CouponDO couponDO = couponMapper.selectById(requestParam.getCouponId());
        if (Objects.isNull(couponDO)) {
            throw new ClientException("不存在该优惠券。。");
        }
        TaskDO taskDO = BeanUtil.toBean(requestParam, TaskDO.class);
        taskDO.setTaskStatus(1);
        taskDO.setOperatorId(UserContext.getFakeUserId());
        baseMapper.insert(taskDO);
        Message<TaskDO> message = MessageBuilder.withPayload(taskDO)
                .setHeader(MessageConst.PROPERTY_TAGS, "task-A")
                .setHeader(MessageConst.PROPERTY_KEYS, UUID.randomUUID().toString())
                .build();
        if (taskDO.getSendType().equals(0)) {
            //立即发送
            rocketMQTemplate.syncSend("zrq-spc-task-excel-topic", message);
        } else {
            //延迟发送
            rocketMQTemplate.syncSendDelayTimeMills("zrq-spc-task-excel-topic", message, DateUtil.betweenMs(new Date(), taskDO.getSendTime()));
        }
    }
}
