package com.example.beanfactory;

import com.example.annotation.Component;
import com.example.classloader.ResourceLoader;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zrq
 * @time 2025/8/23 15:40
 * @description
 */
public abstract class AbstractApplicationContext implements BeanFactory {

    protected Set<Class<?>> canInitClz(Class<?> appClz, String[] args) {
        return filterClz(ResourceLoader.getClasses(appClz.getPackageName()));
    }

    private Set<Class<?>> filterClz(Set<Class<?>> allClzName) {
        return allClzName.stream().filter(clzName -> clzName.isAnnotationPresent(Component.class)).collect(Collectors.toSet());
    }

    @Override
    public Object getBean(String name) {
        return null;
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
}
