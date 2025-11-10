package com.ds.designpatterns.factory.factorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:17
 * @description
 */
public class TeslaFactory implements BaseFactory {
    @Override
    public Car createCar() {
        return new Tesla();
    }
}
