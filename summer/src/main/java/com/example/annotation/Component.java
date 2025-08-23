package com.example.annotation;

import java.lang.annotation.*;

/**
 * @author zrq
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Component {
}
