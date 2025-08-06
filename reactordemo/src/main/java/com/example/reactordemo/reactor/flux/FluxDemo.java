package com.example.reactordemo.reactor.flux;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author zrq
 * @time 2025/8/5 21:00
 * @description
 */
public class FluxDemo {
    public static void main(String[] args) {
//        Flux.range(1,10)
//                .buffer(3)
//                .doOnNext(System.out::println)
//                .subscribe(i-> System.out.println("i = " + i));

        Flux<List<Integer>> buffer =
                Flux.range(1, 10).buffer(2);
        buffer.subscribe(new BaseSubscriber<>() {
            @Override
            protected void hookOnNext(List<Integer> value) {
                System.out.println("value = " + value);
                request(2);
            }

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("subscription = " + subscription);
                request(2);
            }
        });
    }
}
