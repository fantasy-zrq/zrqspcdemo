package com.ds.newdesignpatterns.factory.factorymethod;

import lombok.Getter;

/**
 * @author zrq
 * @time 2025/11/10 15:19
 * @description
 */
@Getter
public class Cadillac implements Car{
    public String name = "Cadillac";

    @Override
    public void show() {
        System.out.println(name);
    }
}
