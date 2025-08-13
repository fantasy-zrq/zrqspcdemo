package com.example.nettydemo.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zrq
 * @time 2025/8/13 14:23
 * @description
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class MyTaskDemo {
    private final ThreadPoolExecutor myExecutor;

    public void runTask(Runnable task) {
        if (Objects.isNull(task)) {
            throw new NullPointerException("task为null");
        }
        log.info("runTask的task执行----[{}]", task);
        myExecutor.execute(task);
    }

    public Future<?> runFeature(Callable<?> task) {
        if (Objects.isNull(task)) {
            throw new NullPointerException("task为null");
        }
        return myExecutor.submit(task);
    }
}
