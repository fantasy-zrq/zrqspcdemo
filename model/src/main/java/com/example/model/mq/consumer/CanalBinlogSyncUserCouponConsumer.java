package com.example.model.mq.consumer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.model.common.exception.ClientException;
import com.example.model.dto.req.CanalBinlogEvent;
import com.example.model.dto.req.CouponDelayCancelRocketMqDTO;
import com.example.model.mq.producer.RocketMqCouponDelayCancelProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY;

/**
 * @author zrq
 * @time 2025/11/20 19:28
 * @description
 */
@RocketMQMessageListener(
        topic = "zrq-spc-canal-listener-topic-rebuild-A",
        consumerGroup = "zrq-spc-canal-listener-consumer-group-rebuild-A"
)
@RequiredArgsConstructor
@Component
@Slf4j(topic = "CanalBinlogSyncUserCouponConsumer")
public class CanalBinlogSyncUserCouponConsumer implements RocketMQListener<CanalBinlogEvent> {

    private final RocketMqCouponDelayCancelProducer rocketMqCouponDelayCancelProducer;
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${coupon.consistency.canalStrategy}")
    private String canalStrategy;

    @Override
    public void onMessage(CanalBinlogEvent message) {
        log.info("binlog-组件开始消费----");
        if (ObjectUtil.notEqual(canalStrategy, "canal")) {
            log.info("isCanal----->[{}]", canalStrategy);
            return;
        }
        Map<String, Object> first = CollUtil.getFirst(message.getData());
        first.forEach((key, value) -> log.info("key=>{},value=>{}", key, value));
        String couponId = first.get("coupon_id").toString();
        String userId = first.get("user_id").toString();
        if (Objects.equals(message.getType(), "INSERT")) {
            String receiveKey = String.format(REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY, userId);
            stringRedisTemplate.opsForZSet().add(receiveKey, String.valueOf(couponId), Double.parseDouble(String.valueOf(System.currentTimeMillis())));
            Double score;
            try {
                score = stringRedisTemplate.opsForZSet().score(receiveKey, String.valueOf(couponId));
                //代表上一步redis新增失败了
                if (score == null) {
                    stringRedisTemplate.opsForZSet().add(receiveKey, String.valueOf(couponId), Double.parseDouble(String.valueOf(System.currentTimeMillis())));
                }
            } catch (Exception e) {
                log.error("redis--执行新增错误");
                //RocketMQ延迟队列来补偿ZSet
                throw new ClientException("redis--执行新增错误");
            }
            String endTimeStr = (String) first.get("end_time");
            DateTime endTime = DateUtil.parse(endTimeStr, "yyyy-MM-dd HH:mm:ss");
            long delay_time = DateUtil.between(new Date(), endTime, DateUnit.SECOND);
            log.info("delay_time == >[{}]", delay_time);
            CouponDelayCancelRocketMqDTO cancelRocketMqDTO = CouponDelayCancelRocketMqDTO.builder()
                    .couponId(Long.valueOf(couponId))
                    .userId(Long.valueOf(userId))
                    .delayTime(delay_time)
                    .build();
            SendResult sendResult = rocketMqCouponDelayCancelProducer.sendMessage(cancelRocketMqDTO);
            if (!Objects.equals(sendResult.getSendStatus().name(), "SEND_OK")) {
                log.error("延迟取消优惠券任务推送失败---sendRes-->[{}]", sendResult.getSendStatus().name());
                throw new ClientException("延迟取消优惠券任务推送失败---");
            }
        }
    }
}
