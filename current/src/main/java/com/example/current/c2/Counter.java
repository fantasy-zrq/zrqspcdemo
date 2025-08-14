package com.example.current.c2;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zrq
 * @time 2025/8/14 16:39
 * @description
 */

@Slf4j
public class Counter {
    public final List<Product> products = new ArrayList<>();

    @SneakyThrows
    public synchronized void addProduct(Product product) {
        while (products.size() >= 10) {
            try {
                log.info("products.size-------[{}],进入wait", products.size());
                wait();
                log.info("products.size-------[{}]，wait结束，被唤醒了", products.size());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        products.add(product);
        Thread.sleep(200L);
        //唤醒consumer线程,一定使用notifyAll，而不是notify避免死锁
        notifyAll();
        log.info("生产product--->[{}]----size-->[{}]", product, products.size());

    }

    @SneakyThrows
    public synchronized Product removeProduct() {
        while (products.isEmpty()) {
            //唤醒生产者线程
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("products.size-------[{}],notify生产者", products.size());
        }
        //永远取队头
        log.info("消费产品，counter容量为------>[{}]", products.size());
        //一定使用notifyAll，而不是notify避免死锁
        notifyAll();
        Product product = products.remove(0);
        Thread.sleep(500L);
        return product;
    }

    @SneakyThrows
    public static void main(String[] args) {
        Counter counter = new Counter();
        new Thread(() -> {
            while (true) {
                counter.addProduct(new Product("cake", "black"));
            }
        }).start();
        new Thread(() -> {
            while (true) {
                counter.removeProduct();
            }
        }).start();
    }
}
