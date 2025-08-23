package com.example.beanfactory;

import cn.hutool.core.util.StrUtil;
import com.example.annotation.Component;
import com.example.beandef.BeanDefinition;
import com.example.classloader.ResourceLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zrq
 * @time 2025/8/23 15:40
 * @description
 */
public abstract class AbstractApplicationContext implements BeanFactory {

    //封装BeanDefinition的Map
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    //封装创建完成bean的map
    private final Map<String, Object> beanMap = new HashMap<>();

    protected Set<Class<?>> canInitClz(Class<?> appClz, String[] args) {
        return filterClz(ResourceLoader.getClasses(appClz.getPackageName()));
    }

    private Set<Class<?>> filterClz(Set<Class<?>> allClzName) {
        return allClzName.stream().filter(clzName -> clzName.isAnnotationPresent(Component.class)).collect(Collectors.toSet());
    }

    @Override
    public Object getBean(String name) {
        Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("name is null！！"));
        return beanMap.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    public void registerBeanDefinition(Set<Class<?>> classes) {
        classes.forEach(clz -> {
            String beanName = StrUtil.lowerFirst(clz.getSimpleName());
            BeanDefinition beanDefinition = new BeanDefinition(beanName, clz);
            beanDefinitionMap.put(beanName, beanDefinition);
        });
    }

    public void createBean() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            Class<?> clz = beanDefinition.getBeanType();
            Constructor<?> constructor = clz.getConstructor();
            Object o = constructor.newInstance();
            beanMap.put(beanDefinition.getBeanName(), o);
        }
    }
}
