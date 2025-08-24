package com.example.beandef;

import com.example.annotation.Autowired;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private Map<Field, String> typeFieldsMap = new HashMap<>();

    public BeanDefinition(String beanName, Class<?> beanType) {
        this.beanName = beanName;
        this.beanType = beanType;
        fillMap(beanName, beanType);
    }

    private void fillMap(String beanName, Class<?> beanType) {
        Arrays.stream(beanType.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Autowired.class)) {
                typeFieldsMap.put(field,
                        "".equals(field.getAnnotation(Autowired.class).value()) ? field.getName() : "");
            }
        });
    }

    public List<Field> getAutowiredFields() {
        return typeFieldsMap.keySet().stream().toList();
    }
}
