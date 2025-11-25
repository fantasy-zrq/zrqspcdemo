package com.ds.newdesignpatterns.factory.abstractfactorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:42
 * @description
 */
public class LuckyFactory implements BaseFactory{
    @Override
    public Americano productionAmericano() {
        return new LimeAmericano();
    }

    @Override
    public Latte productionLatte() {
        return new FreshMilkLatte();
    }
}
