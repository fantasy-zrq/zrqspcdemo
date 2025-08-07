package com.example.model.common.annotation.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/8/7 14:52
 * @description
 */
@Slf4j
@Aspect
@Component
public class LogConsumerTimeAspect {

    @Around("@annotation(com.example.model.common.annotation.LogConsumerTime)")
    public Object logConsumerTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("method:【{}】--->花费时间:[{}]ms", methodName, end - start);
        return result;
    }
}
