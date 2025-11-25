package com.ds.newdesignpatterns.factory.factorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:19
 * @description
 */
public class CadillacFactory implements BaseFactory {
    @Override
    public Car createCar() {
        return new Cadillac();
    }
}
