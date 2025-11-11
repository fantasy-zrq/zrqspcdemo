package com.ds.designpatterns.factory.factorymethod;

import lombok.Getter;

/**
 * @author zrq
 * @time 2025/11/10 15:18
 * @description
 */
@Getter
public class Tesla implements Car{
    String name = "Tesla";

    @Override
    public void show() {
        System.out.println(name);
    }
}
