package com.example.model;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/9/27 15:45
 * @description
 */
@Slf4j
public class TestSupplier {

    public void test1(MySupplier<String> stringMySupplier){
        String res = stringMySupplier.supplier();
        log.info("res-->{}",res);
    }
}

@Slf4j
class Demo{
    public static void main(String[] args) {
        TestSupplier testSupplier = new TestSupplier();
        testSupplier.test1(()-> "hello supplier!!!");
    }

    public void sum(){
        int num = 0;
        while (num <= 3000){
            num++;
            log.info("num-->{}",num);
        }
        log.info("num-->{}",num);
    }
}
