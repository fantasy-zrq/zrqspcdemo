package com.example.model.executor.queue;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.example.model.dto.req.CouponRemindDTO;
import com.example.model.dto.req.CouponRemindDelayEvent;
import com.example.model.mq.producer.RocketMqCouponRemindProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * @author zrq
 * @time 2025/11/21 22:19
 * @description
 */
@Slf4j(topic = "RedisCouponRemindConsumer")
@Component
@RequiredArgsConstructor
public class RedisCouponRemindConsumer implements CommandLineRunner {

    private final RocketMqCouponRemindProducer rocketMqCouponRemindProducer;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

    @Override
    public void run(String... args) throws Exception {
        Executors.newSingleThreadExecutor(
                        runnable -> {
                            Thread thread = new Thread(runnable);
                            thread.setName("delay_coupon-remind_consumer");
                            thread.setDaemon(Boolean.TRUE);
                            return thread;
                        })
                .execute(() -> {
                    RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque("coupon_remind_block_queue");
                    for (; ; ) {
                        try {
                            // 获取延迟队列待消费 Key
                            String key = blockingDeque.take();
                            log.info("获取到延时队列消费失败的key---->[{}]", key);
                            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
                                log.info("检查用户发送的通知消息Key：{} 未消费完成，开启重新投递", key);

                                // Redis 中还存在该 Key，说明任务没被消费完，则可能是消费机器宕机了，重新投递消息
                                CouponRemindDTO remindDTO = JSONUtil.toBean(stringRedisTemplate.opsForValue().get(key), CouponRemindDTO.class);

                                CouponRemindDelayEvent event = BeanUtil.toBean(remindDTO, CouponRemindDelayEvent.class);
                                event.setDelayTime(1L);
                                rocketMqCouponRemindProducer.sendMessage(event);

                                // 提醒用户后删除 Key
                                stringRedisTemplate.delete(key);
                            }
                        } catch (Throwable ignored) {
                        }
                    }
                });
    }
}
