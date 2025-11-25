package com.ds.newdesignpatterns.factory.abstractfactorymethod;

/**
 * @author zrq
 * @time 2025/11/10 15:39
 * @description
 */
public class FreshMilkLatte implements Latte{
    @Override
    public void show() {
        System.out.println("I am lucky FreshMilkLatte");
    }
}
