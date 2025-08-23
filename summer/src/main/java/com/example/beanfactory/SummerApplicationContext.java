package com.example.beanfactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * @author zrq
 * @time 2025/8/23 14:46
 * @description 包含所有的beanFactory
 */
public class SummerApplicationContext extends AbstractApplicationContext {

    private SummerApplicationContext(Class<?> appClz, String[] args) {
        try {
            init(canInitClz(appClz, args));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static SummerApplicationContext run(Class<?> appClz, String[] args) {
        return new SummerApplicationContext(appClz, args);
    }

    private void init(Set<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //注册所有beanDefinition
        registerBeanDefinition(classes);
        //根据beanDefinition创建bean对象
        createBean();
    }
}
