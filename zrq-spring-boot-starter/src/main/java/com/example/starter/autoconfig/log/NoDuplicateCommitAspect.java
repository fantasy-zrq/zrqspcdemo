package com.example.starter.autoconfig.log;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * @author zrq
 * @time 2025/9/1 13:56
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
public class NoDuplicateCommitAspect {
    //    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(com.example.starter.autoconfig.log.NoDuplicateCommit)")
    public Object noDuplicateCommitImpl(ProceedingJoinPoint joinPoint) {
        String format = "zrq:spc:starter:path:%s:userid:%s";
        String servletPath = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServletPath();
        String lockKey = String.format(format, servletPath, "123456789");

        log.info("lockKey--->{}", lockKey);
//        RLock lock = redissonClient.getLock(lockKey);
        //使用setnx优化幂等性，redissonClient太重了幂等性
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "Idempotence", 1L, TimeUnit.SECONDS))) {
            log.error("请求提交重复-获取锁失败");
            throw new DuplicateRequestException("请求提交重复");
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
