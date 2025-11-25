package com.ds.newdesignpatterns.factory.factorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:21
 * @description
 */
public class TestFactoryMethod {
    public static void main(String[] args) {
        CadillacFactory cadillacFactory = new CadillacFactory();
        Car cadillac = cadillacFactory.createCar();
        cadillac.show();
    }
}
