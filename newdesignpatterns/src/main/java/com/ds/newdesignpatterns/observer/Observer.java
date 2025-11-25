package com.ds.newdesignpatterns.observer;

/**
 * 观察者，监听被观察者的变动
 */
@FunctionalInterface
public interface Observer {

    void update(Event event);
}
