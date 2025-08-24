package com.example.beanfactory;

import cn.hutool.core.util.StrUtil;
import com.example.annotation.Component;
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
        }
        Class<?> clz = beanDefinition.getBeanType();
        List<Field> fields = beanDefinition.getAutowiredFields();
        Constructor<?> constructor = clz.getConstructor();
        //这个bean对象是没有注入属性的对象
        Object bean = constructor.newInstance();
        //放入二级缓存
        earlyBeanMap.put(beanName, bean);

        if (fields.isEmpty()) {
            //代表没有需要注入的属性
            beanMap.put(beanName, bean);
            earlyBeanMap.remove(beanName);
            return bean;
        }
        for (Field diField : fields) {
            //先解决通过名称注入
            String diName = diField.getName();
            if (!earlyBeanMap.containsKey(diName)) {
                if (!beanMap.containsKey(diName)) {
                    //把依赖的bean创建了
                    createOneBean(beanDefinitionMap.get(diName));
                }
            }
            //earlyBeanMap中没有，但是beanMap中有---已经付完值的bean对象
            if(earlyBeanMap.containsKey(diName)){
                diField.set(bean, earlyBeanMap.get(diName));
                earlyBeanMap.remove(diName);
            }else if(beanMap.containsKey(diName)){
                diField.set(bean, beanMap.get(diName));
            }
        }
        beanMap.put(beanName, bean);
        return bean;
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
