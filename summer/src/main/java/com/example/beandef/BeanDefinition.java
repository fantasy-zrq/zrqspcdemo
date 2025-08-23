package com.example.beandef;

import com.example.annotation.Autowired;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zrq
 * @time 2025/8/23 14:46
 * @description
 */
@Data
public class BeanDefinition {
    private String beanName;

    private Class<?> beanType;
    //key是需要注入的字段类型，v是名称
    private Map<Class<?>, String> typeFieldsMap = new HashMap<>();

    public BeanDefinition(String beanName, Class<?> beanType) {
        this.beanName = beanName;
        this.beanType = beanType;
        fillMap(beanName, beanType);
    }

    private void fillMap(String beanName, Class<?> beanType) {
        Arrays.stream(beanType.getDeclaredFields()).forEach(field -> {
            if (field.isAnnotationPresent(Autowired.class)) {
                typeFieldsMap.put(field.getType(),
                        "".equals(field.getAnnotation(Autowired.class).value()) ? field.getName() : "");
            }
        });
    }
}
