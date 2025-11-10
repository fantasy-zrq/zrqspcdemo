package com.ds.designpatterns.factory.abstractfactorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:38
 * @description
 */
public class LimeAmericano implements Americano{
    @Override
    public void show() {
        System.out.println("I am lucky lime Americano");
    }
}
