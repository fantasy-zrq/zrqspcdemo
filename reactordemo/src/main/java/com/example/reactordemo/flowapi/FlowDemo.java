package com.example.reactordemo.flowapi;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * @author zrq
 * @time 2025/8/5 17:04
 * @description
 */
@Slf4j
public class FlowDemo {
    public static void main(String[] args) throws IOException {

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {

            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.info("当前线程--->{}--subscription = {}", Thread.currentThread(), subscription);
                subscription.request(1);
            }

            @Override
            public void onNext(String message) {
                log.info("订阅者subscriber接收到了数据--{}", message);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("订阅者subscriber接收到了错误信息--{}", throwable.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("订阅者subscriber完成了所有消息的接收");
            }
        };

        publisher.subscribe(subscriber);

        for (int i = 0; i < 10; i++) {
            publisher.submit("p-" + i);
        }

        publisher.close();

        System.in.read();
    }
}
