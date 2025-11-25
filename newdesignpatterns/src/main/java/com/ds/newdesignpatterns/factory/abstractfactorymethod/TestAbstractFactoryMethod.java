package com.ds.newdesignpatterns.factory.abstractfactorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:44
 * @description
 */
public class TestAbstractFactoryMethod {
    public static void main(String[] args) {
        BaseFactory mannerFactory = new MannerFactory();
        Latte latte = mannerFactory.productionLatte();
        Americano americano = mannerFactory.productionAmericano();
        latte.show();
        americano.show();

        BaseFactory luckyFactory = new LuckyFactory();
        Latte latte1 = luckyFactory.productionLatte();
        Americano americano1 = luckyFactory.productionAmericano();
        latte1.show();
        americano1.show();
    }
}
