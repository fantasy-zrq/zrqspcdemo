package com.ds.newdesignpatterns.observer;

import com.alibaba.fastjson2.JSON;

/**
 * @author zrq
 * @time 2025/11/25 21:12
 * @description
 */
public class TestObserver {
    public static void main(String[] args) {
        Observer msg = new MsgObserver();
        Observer email = new EmailObserver();

        ConcreteSubject subject = new ConcreteSubject();
        subject.addObserver(msg);
        subject.addObserver(event -> {
            System.out.println(event + "=" + JSON.toJSONString(event));
            System.out.println("匿名类Observer被触发");
        });
        subject.addObserver(email);

        subject.hook(1);
    }
}
