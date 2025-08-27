package com.example.aop;

import cn.hutool.core.bean.BeanUtil;
import com.example.annotation.MyLog;
import lombok.Getter;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author zrq
 * @time 2025/8/24 16:44
 * @description
 */
public class AopFactory implements ObjectFactory {

    @Getter
    private final Object originalBean;

    public AopFactory(Object originalBean) {
        this.originalBean = originalBean;
    }

    @Override
    public Object getObject(Object bean, String beanName) {
        if (Objects.isNull(bean)) {
            return originalBean;
        }
        Class<?> clz = bean.getClass();
        if (shouldProxy(clz)) {
            return createProxyBean(bean, beanName);
        }
        return bean;
    }

    private Object createProxyBean(Object originalBean, String beanName) {
        try {
            Class<?> proxyClass = new ByteBuddy()
                    .subclass(originalBean.getClass())
                    .method(ElementMatchers.isAnnotatedWith(MyLog.class))
                    .intercept(MethodDelegation.to(new Object() {
                        @RuntimeType
                        public Object intercept(@SuperCall Callable<Object> zuper,
                                                @Origin Method method,
                                                @AllArguments Object[] args) throws Exception {
                            System.out.println("[ByteBuddy-AOP] before method: " + method.getName());
                            Object result = zuper.call(); // 调用原始对象的方法
                            System.out.println("[ByteBuddy-AOP] after method: " + method.getName());
                            return result;
                        }
                    }))
                    .make()
                    .load(originalBean.getClass().getClassLoader())
                    .getLoaded();

            // 关键：通过原始对象的构造函数或setter复制属性到代理对象
            Object proxy = proxyClass.getDeclaredConstructor().newInstance();
            BeanUtil.copyProperties(originalBean, proxy); // 假设使用Spring的BeanUtils复制属性
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException("创建代理失败: " + beanName, e);
        }
    }

    private boolean shouldProxy(Class<?> clz) {
        for (Method method : clz.getMethods()) {
            if (method.isAnnotationPresent(MyLog.class)) {
                return true;
            }
        }
        return false;
    }
}
