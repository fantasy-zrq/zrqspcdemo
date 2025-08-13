package com.example.nettydemo.nettytimewheel;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zrq
 * @time 2025/8/13 15:36
 * @description
 */
@Slf4j
public class TimerWheel {
    private volatile long startTime;
    private final Slot[] wheel;
    private final Ticker ticker;
    private final AtomicBoolean started;
    private final CountDownLatch startCountDownLatch;

    public TimerWheel() {
        wheel = new Slot[10];
        ticker = new Ticker();
        started = new AtomicBoolean(false);
        startCountDownLatch = new CountDownLatch(1);
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new Slot();
        }
    }

    private void start() {
        if (started.compareAndSet(false, true)) {
            log.error("时间轮启动...");
            ticker.start();
            //为了保证addDelayTask中不会异常
        }
        while (startTime == 0) {
            try {
                startCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (!started.compareAndSet(true, false)) {
            LockSupport.unpark(ticker);
            log.error("当前时间轮已经结束..");
        }
    }

    public void addDelayTask(Runnable task, long delayMs) {
        start();
        DelayTask delayTask = new DelayTask(task, delayMs);
        //286ms在第2个slot槽位
        int index = (int) (((delayTask.deadLine - startTime) % 100) % wheel.length);
        Slot slot = wheel[index];
        slot.pushDelayTask(delayTask);
    }


    public static class Slot {

        private DelayTask head;
        private DelayTask tail;

        public void runWithDeadLine(long tickTime) {
            DelayTask current = head;
            while (current != null) {
                DelayTask next = head.next;
                if (current.deadLine < tickTime) {
                    removeTask(current);
                    current.runnable.run();
                }
                current = next;
            }
        }

        private void removeTask(DelayTask current) {
            if (current == null) {
                return;
            }

            if (current == head) {
                head = head.next;
                if (head != null) {
                    head.pre = null;
                } else {
                    tail = null; // 链表空了
                }
            } else if (current == tail) {
                tail = tail.pre;
                if (tail != null) {
                    tail.next = null;
                } else {
                    head = null; // 链表空了
                }
            } else {
                DelayTask pre = current.pre;
                DelayTask next = current.next;
                pre.next = next;
                next.pre = pre;
            }

            current.pre = null;
            current.next = null;
        }

        public void pushDelayTask(DelayTask delayTask) {
            if (head == null) {
                head = tail = delayTask;
            } else {
                //尾插   null<-A<->B<->C<->D->null
                //            ||          ||
                //            head       tail
                tail.next = delayTask;
                delayTask.pre = tail;
                tail = delayTask;
            }
        }
    }

    public static class DelayTask {
        private final Runnable runnable;
        private final long deadLine;
        private DelayTask next;
        private DelayTask pre;

        public DelayTask(Runnable runnable, long delayMs) {
            this.runnable = runnable;
            this.deadLine = System.currentTimeMillis() + delayMs;
        }
    }

    public class Ticker extends Thread {

        int tickCount = 0;

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            startCountDownLatch.countDown();
            //时间轮的精度是100ms
            while (started.get()) {
                long tickTime = startTime + (tickCount + 1) * 100L;
                //代表还没到该滴答的时候
                while (System.currentTimeMillis() < tickTime) {
                    LockSupport.parkUntil(tickTime);
                    if (!started.get()) {
                        return;
                    }
                }
                Slot slot = wheel[tickCount % wheel.length];
                slot.runWithDeadLine(tickTime);
                tickCount++;
            }
        }
    }
}
