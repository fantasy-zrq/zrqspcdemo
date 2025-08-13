package com.example.nettydemo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zrq
 * @time 2025/8/13 10:09
 * @description
 */
@Slf4j
@Component
public class ScheduleDemo {

    private final AtomicInteger atomicInteger1 = new AtomicInteger();
    private final AtomicInteger atomicInteger2 = new AtomicInteger();
    private final AtomicInteger atomicInteger3 = new AtomicInteger();

    @Scheduled(fixedRate = 500L)
    public void task1() throws InterruptedException {
        log.info("任务1执行次数--->[{}],线程名称=={}", atomicInteger1.getAndIncrement(), Thread.currentThread().getName());
        Thread.sleep(500000L);
    }

    @Scheduled(fixedRate = 500L)
    public void task2() {
        log.info("任务2执行次数--->[{}],线程名称=={}", atomicInteger2.getAndIncrement(), Thread.currentThread().getName());
    }


    @Scheduled(fixedRate = 500L)
    public void task3() {
        log.info("任务3执行次数--->[{}],线程名称=={}", atomicInteger3.getAndIncrement(), Thread.currentThread().getName());
    }
}
