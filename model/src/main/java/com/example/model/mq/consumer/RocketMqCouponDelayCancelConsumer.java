package com.example.model.mq.consumer;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.StrBuilder;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.model.base.MessageWrapper;
import com.example.model.common.exception.ClientException;
import com.example.model.dto.req.CouponDelayCancelRocketMqDTO;
import com.example.model.entity.ReceiveDO;
import com.example.model.entity.mapper.ReceiveMapper;
import com.example.starter.autoconfig.log.NoMQDuplicateConsume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_COUPON_DISTRIBUTION_LIMIT_KEY;
import static com.example.model.common.constance.RedisConstanceKey.REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY;

/**
 * @author zrq
 * @time 2025/11/19 20:58
 * @description
 */
@RocketMQMessageListener(
        topic = "zrq-spc-task-excel-topic-batch-distribution-rebuild-A",
        consumerGroup = "zrq-spc-task-coupon-expire-delay-cancel-consumer-group-rebuild-A",
        selectorExpression = "zrq-spc-task-coupon-expire-delay-cancel-tag"
)
@RequiredArgsConstructor
@Component
@Slf4j(topic = "RocketMqCouponDelayCancelConsumer")
public class RocketMqCouponDelayCancelConsumer implements RocketMQListener<MessageWrapper<CouponDelayCancelRocketMqDTO>> {

    //RocketMqCouponDelayCancelConsumer不消费？
    private final StringRedisTemplate stringRedisTemplate;
    private final ReceiveMapper receiveMapper;
    private static final String LUA_SCRIPT = "lua/coupon_expire_cancel_script.lua";

    @NoMQDuplicateConsume(
            keyPrefix = "coupon_expire_cancel_execute:idempotent:",
            key = "#message.msg.couponId",
            keyTimeout = 60 * 2L
    )
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(MessageWrapper<CouponDelayCancelRocketMqDTO> message) {
        log.info("RocketMqCouponDelayCancelConsumer进入消费逻辑---message-->[{}]", JSON.toJSONString(message));
        CouponDelayCancelRocketMqDTO cancelRocketMqDTO = message.getMsg();
        Long couponId = cancelRocketMqDTO.getCouponId();
        Long userId = cancelRocketMqDTO.getUserId();
        Long delayTime = cancelRocketMqDTO.getDelayTime();
        if (delayTime == null) {
            throw new ClientException("延迟时间为null");
        }

        DefaultRedisScript<Long> script = Singleton.get(LUA_SCRIPT, () -> {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_SCRIPT)));
            redisScript.setResultType(Long.class);
            return redisScript;
        });

        String receiveUserKey = String.format(REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY, userId);
        String limitKey = StrBuilder.create().append(REDIS_COUPON_DISTRIBUTION_LIMIT_KEY)
                .append(userId)
                .append("_")
                .append(couponId).toString();

        stringRedisTemplate.execute(script, List.of(receiveUserKey, limitKey), couponId);
        receiveMapper.update(null, Wrappers.lambdaUpdate(ReceiveDO.class)
                .eq(ReceiveDO::getUserId, userId)
                .eq(ReceiveDO::getCouponId, couponId)
                .set(ReceiveDO::getStatus, 2));
    }
}
