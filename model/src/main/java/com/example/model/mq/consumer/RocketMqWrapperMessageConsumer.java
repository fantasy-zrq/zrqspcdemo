//package com.example.model.mq.consumer;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.example.model.base.MessageWrapper;
//import com.example.model.base.RocketMqExcelMessageCheckDTO;
//import com.example.model.dto.req.ExcelReqDTO;
//import com.example.model.executor.ExcelResolverThreadPool;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
///**
// * @author zrq
// * @time 2025/9/5 11:11
// * @description
// */
//@RocketMQMessageListener(
//        consumerGroup = "zrq-spc-mock-excel-consumer-group",
//        topic = "zrq-spc-mock-excel-topic"
//)
//@Component
//@Slf4j(topic = "RocketMqWrapperMessageConsumer")
//@RequiredArgsConstructor
//public class RocketMqWrapperMessageConsumer implements RocketMQListener<MessageWrapper<RocketMqExcelMessageCheckDTO>> {
//    private final StringRedisTemplate stringRedisTemplate;
//    private final ExcelResolverThreadPool excelResolverThreadPool;
//
//    @Override
//    public void onMessage(MessageWrapper<RocketMqExcelMessageCheckDTO> message) {
//        log.info("RocketExcelConsumer 开始消费消息-->[{}]", message);
//        if (Objects.equals(stringRedisTemplate.hasKey("zrq:spc:mock:excel:rowcount"), Boolean.FALSE)) {
//            //代表插入任务失败了得重试
//            log.info("RedisDelayQueueConsumer 进行excel行数插入重试~~~");
//            excelResolverThreadPool.execute(BeanUtil.copyProperties(message.getMsg(), ExcelReqDTO.class));
//            return;
//        }
//        log.info("redis中已经存入数据--->[{}]", message);
//    }
//}
