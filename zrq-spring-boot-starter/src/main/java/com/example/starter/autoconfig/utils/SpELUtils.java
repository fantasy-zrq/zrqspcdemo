package com.example.starter.autoconfig.utils;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author zrq
 * @time 2025/11/17 20:49
 * @description
 */
public class SpELUtils {

    public static String parse(String key, MethodSignature signature, Object[] args) {
        List<String> rule = List.of("#");
        Optional<String> first = rule.stream().filter(key::contains).findFirst();
        if (first.isPresent()) {
            return parseSpEL(key, signature, args);
        }
        return key;
    }

    private static String parseSpEL(String key, MethodSignature signature, Object[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(key);
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 1. 尝试获取参数名
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(signature.getMethod());

        // 2. 如果直接获取失败（比如是接口方法），尝试获取类上的实现方法
        if (parameterNames == null) {
            try {
                // 尝试从声明类中获取同名同参数类型的方法
                Method targetMethod = signature.getDeclaringType()
                        .getDeclaredMethod(signature.getName(), signature.getParameterTypes());
                parameterNames = discoverer.getParameterNames(targetMethod);
            } catch (Exception e) {
                // 忽略异常，降级处理
            }
        }

        // 3. 绑定参数到上下文
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }

        // 4. 【关键修复】总是绑定 arg0, arg1... 以及 a0, a1... 作为备用别名
        // 这样即使参数名丢失，你也可以把注解改成 key = "#a0.msg.couponId" 来临时修复
        for (int i = 0; i < args.length; i++) {
            context.setVariable("a" + i, args[i]);   // 支持 #a0
            context.setVariable("p" + i, args[i]);   // 支持 #p0
            context.setVariable("arg" + i, args[i]); // 支持 #arg0
        }

        return expression.getValue(context, String.class);
    }
}
