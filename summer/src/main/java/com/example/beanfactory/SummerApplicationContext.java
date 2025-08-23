package com.example.beanfactory;

import com.example.beandef.BeanDefinition;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zrq
 * @time 2025/8/23 14:46
 * @description 包含所有的beanFactory
 */
public class SummerApplicationContext extends AbstractApplicationContext {

    //封装BeanDefinition的Map
    @Getter
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    //封装创建完成bean的map
    private final Map<Class<?>, Object> beanMap = new HashMap<>();

    private SummerApplicationContext(Class<?> appClz, String[] args) {
        init(canInitClz(appClz, args));
    }

    public static SummerApplicationContext run(Class<?> appClz, String[] args) {
        return new SummerApplicationContext(appClz, args);
    }

    private void init(Set<Class<?>> classes) {
        //注册所有beanDefinition
        registerBeanDefinition(classes);
    }

    private void registerBeanDefinition(Set<Class<?>> classes) {
        classes.forEach(clz->{
            String beanName = clz.getSimpleName();
            BeanDefinition beanDefinition = new BeanDefinition(beanName, clz);
            beanDefinitionMap.put(beanName,beanDefinition);
        });
    }
}
