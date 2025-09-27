package com.example.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zrq
 * @time 2025/9/16 15:45
 * @description
 */
@SpringBootTest
public class UniqueNameTest {

    @Value("${unique-name:}")
    private String name;

    @Test
    public void test1(){
        System.out.println("name = " + name);
    }
}
