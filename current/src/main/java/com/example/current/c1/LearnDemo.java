package com.example.current.c1;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/13 21:29
 * @description
 */
@Slf4j
public class LearnDemo {

    private int num = 10;

    @SneakyThrows
    public void t1() {
        System.out.println("t1-before");
        synchronized (this) {
            if (num < 2) {
                this.wait();
            }
        }
    }

    @SneakyThrows
    public void t3() {
        System.out.println("t1-before");
        synchronized (Integer.valueOf(num)) {
            if (num < 2) {
                this.wait();
            }
        }
    }

    @SneakyThrows
    public void t2() {
        System.out.println("t2-before");
        synchronized (this) {
            if (num == 2) {
                this.notify();
                //this.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        LearnDemo learnDemo = new LearnDemo();
        new Thread(learnDemo::t1).start();
    }
}



