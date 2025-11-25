package com.ds.newdesignpatterns.observer;

import com.alibaba.fastjson2.JSON;

/**
 * @author zrq
 * @time 2025/11/25 21:04
 * @description
 */
public class MsgObserver implements Observer {

    @Override
    public void update(Event event) {
        System.out.println(event + "=" + JSON.toJSONString(event));
        System.out.println("MsgObserver被触发");
    }
}
