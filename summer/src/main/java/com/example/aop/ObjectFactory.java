package com.example.aop;

/**
 * @author zrq
 * 返回由工厂创建的普通对象或者代理对象
 */
public interface ObjectFactory<T> {
    T getObject (String beanName);
}
