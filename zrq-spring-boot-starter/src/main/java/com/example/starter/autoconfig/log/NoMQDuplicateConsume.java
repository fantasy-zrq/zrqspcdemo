package com.example.starter.autoconfig.log;

import java.lang.annotation.*;

/**
 * rocketmq防止优惠券推送任务重复消费幂等组件
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoMQDuplicateConsume {

    /**
     * 设置防重令牌 Key 前缀
     */
    String keyPrefix() default "";

    /**
     * 通过 SpEL 表达式生成的唯一 Key
     */
    String key();

    /**
     * 设置防重令牌 Key 过期时间，单位秒，默认 1 小时
     */
    long keyTimeout() default 3600L;
}
