package com.ds.newdesignpatterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zrq
 * @time 2025/11/25 21:04
 * @description
 */
public class ConcreteSubject implements Subject {

    List<Observer> observerList = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void delObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObserver(Event event) {
        observerList.forEach(each -> each.update(event));
    }

    public void hook(Integer status) {
        if (status.equals(1)) {
            notifyObserver(new MsgEvent(6665599L, "你好hh"));
        }
    }
}
