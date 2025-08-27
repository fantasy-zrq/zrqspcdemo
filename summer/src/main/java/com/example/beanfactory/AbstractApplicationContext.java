package com.example.beanfactory;

import cn.hutool.core.util.StrUtil;
import com.example.annotation.Component;
import com.example.aop.AopFactory;
import com.example.aop.ObjectFactory;
import com.example.beandef.BeanDefinition;
import com.example.classloader.ResourceLoader;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zrq
 * @time 2025/8/23 15:40
 * @description
 */
@Slf4j
public abstract class AbstractApplicationContext implements BeanFactory {

    //封装BeanDefinition的Map
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    //封装创建完成bean的map--一级缓存
    private final Map<String, Object> beanMap = new HashMap<>();

    //封装早期没有属性赋值bean的map--二级缓存
    private final Map<String, Object> earlyBeanMap = new HashMap<>();

    //封装beanName对应的Factory对象--三级缓存
    private final Map<String, ObjectFactory<?>> factoryMap = new HashMap<>();

    protected Set<Class<?>> canInitClz(Class<?> appClz, String[] args) {
        return filterClz(ResourceLoader.getClasses(appClz.getPackageName()));
    }

    private Set<Class<?>> filterClz(Set<Class<?>> allClzName) {
        return allClzName.stream().filter(clzName -> clzName.isAnnotationPresent(Component.class)).collect(Collectors.toSet());
    }

    @Override
    public Object getBean(String name) {
        Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("name is null！！"));
        if (!beanMap.containsKey(name)) {
            try {
                BeanDefinition beanDefinition = beanDefinitionMap.get(name);
                return createOneBean(beanDefinition);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return beanMap.get(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        Optional.ofNullable(requiredType).orElseThrow(() -> new IllegalArgumentException("type is null！！"));
        String beanName = beanDefinitionMap.values()
                .stream()
                .filter(beanDefinition -> requiredType.isAssignableFrom(beanDefinition.getBeanType()))
                .map(BeanDefinition::getBeanName)
                .findFirst()
                .get();
        log.info("beanName===>{}", beanName);
        return (T) getBean(beanName);
    }

    public void createBean() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            createOneBean(beanDefinition);
        }
    }

    /**
     * A->B  B->A
     */
    private Object createOneBean(BeanDefinition beanDefinition) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String beanName = beanDefinition.getBeanName();
        if (beanMap.containsKey(beanName)) {
            return beanMap.get(beanName);
        } else if (earlyBeanMap.containsKey(beanName)) {
            return earlyBeanMap.get(beanName);
        }

        Class<?> clz = beanDefinition.getBeanType();
        List<Field> fields = beanDefinition.getAutowiredFields();
        Constructor<?> constructor = clz.getConstructor();
        //这个bean对象是没有注入属性的对象
        Object originalBean = constructor.newInstance();
        //放入三级工厂缓存
        factoryMap.put(beanName, new AopFactory(originalBean));

        if (fields.isEmpty()) {
            //判断是否产生aop对象
            ObjectFactory<?> factory = factoryMap.remove(beanName);
            originalBean = factory.getObject(originalBean, beanName);
            beanMap.put(beanName, originalBean);
            return originalBean;
        }

        for (Field diField : fields) {
            //先解决通过名称注入
            String diName = diField.getName();
            Object diBean;
            if (beanMap.containsKey(diName)) {
                diBean = beanMap.get(diName);
                diField.set(originalBean, diBean);
            } else if (earlyBeanMap.containsKey(diName)) {
                diBean = earlyBeanMap.get(diName);
                diField.set(originalBean, diBean);
            } else if (factoryMap.containsKey(diName)) {
                //earlyBeanMap和beanMap都没有--->代表需要注入的对象还没创建
                ObjectFactory<?> factory = factoryMap.remove(diName);
                diBean = factory.getObject(null, diName);
                earlyBeanMap.put(diName, diBean);
                //
            } else {
                //第一次来需要创建
                diBean = createOneBean(beanDefinitionMap.get(diName));
            }
            diField.set(originalBean, diBean);
        }
        ObjectFactory<?> shouldProxyFactory = factoryMap.remove(beanName);
        earlyBeanMap.remove(beanName);
        Object targetBean = shouldProxyFactory != null ?
                shouldProxyFactory.getObject(originalBean, beanName) : originalBean;
        beanMap.put(beanName, targetBean);
        return targetBean;
    }

    public void registerBeanDefinition(Set<Class<?>> classes) {
        classes.forEach(clz -> {
            String beanName = StrUtil.lowerFirst(clz.getSimpleName());
            BeanDefinition beanDefinition = new BeanDefinition(beanName, clz);
            beanDefinitionMap.put(beanName, beanDefinition);
        });
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
