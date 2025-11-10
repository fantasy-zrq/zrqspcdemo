package com.ds.designpatterns.factory.abstractfactorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:43
 * @description
 */
public class MannerFactory implements BaseFactory {
    @Override
    public Americano productionAmericano() {
        return new GrapefruitAmericano();
    }

    @Override
    public Latte productionLatte() {
        return new HazelnutLatte();
    }
}
