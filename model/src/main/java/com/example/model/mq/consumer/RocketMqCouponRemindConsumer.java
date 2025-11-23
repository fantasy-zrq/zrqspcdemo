package com.example.model.mq.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.model.base.MessageWrapper;
import com.example.model.common.utils.CouponTemplateRemindUtil;
import com.example.model.dto.req.CouponRemindDTO;
import com.example.model.dto.req.CouponRemindDelayEvent;
import com.example.model.dto.req.CouponTemplateRemindCreateReqDTO;
import com.example.model.entity.RemindDO;
import com.example.model.entity.mapper.RemindMapper;
import com.example.model.executor.CouponRemindAsyncExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author zrq
 * @time 2025/11/21 20:58
 * @description
 */
@RocketMQMessageListener(
        topic = "zrq-spc-task-excel-topic-batch-distribution-rebuild-A",
        consumerGroup = "zrq-spc-task-remind-user-consumer-group-rebuild-A",
        selectorExpression = "zrq-spc-task-remind-user-rebuild-A"
)
@RequiredArgsConstructor
@Component
@Slf4j(topic = "RocketMqCouponRemindConsumer")
public class RocketMqCouponRemindConsumer implements RocketMQListener<MessageWrapper<CouponRemindDelayEvent>> {

    private final CouponRemindAsyncExecutor couponRemindAsyncExecutor;
    private final RemindMapper remindMapper;
    private final RBloomFilter<String> couponRemindCancelBloomFilter;

    @Override
    public void onMessage(MessageWrapper<CouponRemindDelayEvent> messageWrapper) {
        CouponRemindDelayEvent remindDelayEvent = messageWrapper.getMsg();
        log.info("优惠券预定提醒consumer--->{}", remindDelayEvent);
        CouponRemindDTO remindDTO = BeanUtil.toBean(remindDelayEvent, CouponRemindDTO.class);
        if (isCancelRemind(remindDTO)) {
            log.info("该消息不通知，提醒被取消");
        } else {
            remindDTO.setContact("1919616532");
            couponRemindAsyncExecutor.executeCouponRemind(remindDTO);
        }
    }

    /**
     * 判断用户优惠券提醒是否取消
     *
     * @return ture-取消，false-没取消
     */
    public boolean isCancelRemind(CouponRemindDTO requestParam) {
        CouponTemplateRemindCreateReqDTO bean = BeanUtil.toBean(requestParam, CouponTemplateRemindCreateReqDTO.class);
        boolean contains = couponRemindCancelBloomFilter.contains(JSON.toJSONString(bean));
        if (!contains) {
            //BloomFilter说不存在该取消提醒则一定不存在
            return false;
        }
        LambdaQueryWrapper<RemindDO> queryWrapper = Wrappers.lambdaQuery(RemindDO.class)
                .eq(RemindDO::getUserId, requestParam.getUserId())
                .eq(RemindDO::getCouponId, requestParam.getCouponId());
        RemindDO remindDO = remindMapper.selectOne(queryWrapper);
        if (Objects.isNull(remindDO)) {
            log.info("用户优惠券提醒已经被取消..");
            return true;
        }
        Long combineResult = CouponTemplateRemindUtil.combineTypeAndRemindTime(requestParam.getRemindTime(), requestParam.getType());
        if (Objects.equals(0L, combineResult & remindDO.getInformation())) {
            log.info("该种类型该种时间的优惠券提醒已经被取消");
            return true;
        }
        return false;
    }
}
