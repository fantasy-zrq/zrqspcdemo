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
import java.util.concurrent.Callable;

/**
 * @author zrq
 * @time 2025/8/24 16:44
 * @description 简化版 AOP 工厂
 */
public class AopFactory implements ObjectFactory {
    @Getter
    private final Object originalBean;

    public AopFactory(Object originalBean) {
        this.originalBean = originalBean;
    }

    @Override
    public Object getObject(String beanName) {
        return createProxyBean(originalBean, beanName);
    }

    private Object createProxyBean(Object originalBean, String beanName) {
        try {
            // 使用 ByteBuddy 生成代理类
            Class<?> proxyClass = new ByteBuddy()
                    .subclass(originalBean.getClass())
                    // 只拦截加了 @MyLog 的方法
                    .method(ElementMatchers.isAnnotatedWith(MyLog.class))
                    .intercept(MethodDelegation.to(MyLogInterceptor.class))
                    .make()
                    .load(originalBean.getClass().getClassLoader())
                    .getLoaded();

            // 创建代理实例，并复制原始 Bean 的属性
            Object proxy = proxyClass.getDeclaredConstructor().newInstance();
            BeanUtil.copyProperties(originalBean, proxy);

            return proxy;
        } catch (Exception e) {
            throw new RuntimeException("创建代理失败: " + beanName, e);
        }
    }

    /**
     * 日志拦截器
     */
    public static class MyLogInterceptor {
        @RuntimeType
        public static Object intercept(
                @SuperCall Callable<?> zuper,
                @Origin Method method,
                @AllArguments Object[] args) throws Exception {

            System.out.println("[ByteBuddy-AOP] before method: " + method.getName());
            Object result = null;
            try {
                result = zuper.call(); // 调用原始方法
            } finally {
                System.out.println("[ByteBuddy-AOP] after method: " + method.getName());
            }
            return result; // 如果原方法是 void，这里返回 null，不会报错
        }
    }
}
