package com.example.starter.autoconfig.utils;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

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
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        StandardEvaluationContext context = new StandardEvaluationContext();
        String[] parameterNames = discoverer.getParameterNames(signature.getMethod());
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        return expression.getValue(String.class);
    }
}
