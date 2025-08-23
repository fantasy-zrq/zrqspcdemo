package com.example.beanfactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author zrq
 */
public interface BeanFactory {

    Object getBean(String name) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    <T> T getBean(String name, Class<T> requiredType);

    boolean containsBean(String name);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    Class<?> getType(String name);
}
