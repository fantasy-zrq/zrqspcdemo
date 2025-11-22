package com.example.model.executor;

import com.alibaba.fastjson.JSON;
import com.example.model.common.enums.CouponRemindTypeEnum;
import com.example.model.dto.req.CouponRemindDTO;
import com.example.model.service.impl.MailUserRemind;
import com.example.model.service.impl.PopUpWindowUserRemind;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_COUPON_REMIND_CACHE_KEY;

/**
 * @author zrq
 * @time 2025/11/21 21:45
 * @description
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRemindAsyncExecutor {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() << 1,
            Runtime.getRuntime().availableProcessors() << 2,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private final MailUserRemind mailUserRemind;
    private final PopUpWindowUserRemind popUpWindowUserRemind;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    public void executeCouponRemind(CouponRemindDTO requestParam) {
        executor.execute(() -> {
            Integer type = requestParam.getType();

            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque("coupon_remind_block_queue");
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);

            String remindCacheKey = String.format(REDIS_COUPON_REMIND_CACHE_KEY, requestParam.getUserId(), requestParam.getCouponId(), requestParam.getType(), requestParam.getRemindTime());
            stringRedisTemplate.opsForValue().set(remindCacheKey, JSON.toJSONString(requestParam));
            delayedQueue.offer(remindCacheKey, 15, TimeUnit.SECONDS);

            switch (Objects.requireNonNull(CouponRemindTypeEnum.getByType(type))) {
                case popUpWindow -> popUpWindowUserRemind.remindUser(requestParam);
                case mail -> mailUserRemind.remindUser(requestParam);
                default -> log.error("无该种提醒方式");
            }

            stringRedisTemplate.delete(remindCacheKey);
        });
    }
}
