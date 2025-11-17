package com.example.starter.autoconfig.log;

import com.example.starter.autoconfig.enums.IdempotentMQConsumeStatusEnum;
import com.example.starter.autoconfig.utils.SpELUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zrq
 * @time 2025/11/17 16:48
 * @description
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class NoMQDuplicateConsumeAspect {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String LUA_SCRIPT = """
            local key = KEYS[1]
            local value = ARGV[1]
            local expire_time_ms = ARGV[2]
            return redis.call('SET', key, value, 'NX', 'GET', 'PX', expire_time_ms)
            """;

    @Around("@annotation(NoMQDuplicateConsume)")
    public Object noMQRepeatConsume(ProceedingJoinPoint joinPoint) throws Throwable {
        NoMQDuplicateConsume noMQDuplicateConsume = getNoMQDuplicateConsumeAnnotation(joinPoint);
        String uniqueKey = noMQDuplicateConsume.keyPrefix() + SpELUtils.parse(noMQDuplicateConsume.key(),
                ((MethodSignature) joinPoint.getSignature()), joinPoint.getArgs());

        String absentAndGet = stringRedisTemplate.execute(
                RedisScript.of(LUA_SCRIPT, String.class),
                List.of(uniqueKey),
                IdempotentMQConsumeStatusEnum.CONSUMING.getCode(),
                String.valueOf(TimeUnit.SECONDS.toMillis(noMQDuplicateConsume.keyTimeout()))
        );

        if (Objects.nonNull(absentAndGet)) {
            boolean flag = IdempotentMQConsumeStatusEnum.isError(absentAndGet);
            log.error("消息已经被消费过-->{}", uniqueKey);
            if (flag) {
                throw new RuntimeException("重复消费错误");
            }
        }

        Object res;
        try {
            stringRedisTemplate.opsForValue()
                    .set(uniqueKey, IdempotentMQConsumeStatusEnum.CONSUMED.getCode(), noMQDuplicateConsume.keyTimeout(), TimeUnit.SECONDS);
            res = joinPoint.proceed();
        } catch (Exception e) {
            stringRedisTemplate.delete(uniqueKey);
            throw e;
        }
        return res;
    }

    /**
     * @return 返回自定义防重复消费注解
     */
    public static NoMQDuplicateConsume getNoMQDuplicateConsumeAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(NoMQDuplicateConsume.class);
    }
}
