package com.example.model.xxljob;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.model.entity.ReceiveDO;
import com.example.model.entity.mapper.ReceiveMapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_TASK_DISTRIBUTION_KEY;

/**
 * @author zrq
 * @time 2025/9/13 16:45
 * @description
 */
@RequiredArgsConstructor
@Component
@Slf4j(topic = "RedisMySqlIdenticalTaskHandler")
public class RedisMySqlIdenticalTaskHandler {

    private final StringRedisTemplate stringRedisTemplate;
    private final ReceiveMapper receiveMapper;

    @XxlJob("redisMySqlIdenticalTask")
    public void execute() throws Exception {
        //每次都拉取最近1小时的数据库记录进行同步
        LambdaQueryWrapper<ReceiveDO> wrapper = Wrappers.lambdaQuery(ReceiveDO.class)
                .between(ReceiveDO::getCreateTime, DateUtil.offsetMillisecond(new Date(), -60 * 1000 * 60), DateUtil.now())
                .ge(ReceiveDO::getEndTime, DateUtil.now());
        List<ReceiveDO> receiveDOList = receiveMapper.selectList(wrapper);
        receiveDOList.forEach(each -> {
            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(String.format(REDIS_TASK_DISTRIBUTION_KEY, each.getCouponId(), each.getUserId())))) {
                log.warn("进行redis和MySQL的同步补偿···[{}]", each);
//                stringRedisTemplate.opsForValue().set(String.format(REDIS_TASK_DISTRIBUTION_KEY, each.getCouponId(), each.getUserId()), JSONObject.toJSONString(each)
//                        , DateUtil.between(new Date(), each.getEndTime(), DateUnit.SECOND), TimeUnit.SECONDS);
            }
        });
        log.warn("【优惠券 Redis 一致性补偿任务】执行完成，总记录数={}", receiveDOList.size());
    }
}
