package com.ds.designpatterns.factory.abstractfactorymethod;

public interface BaseFactory {
    Americano productionAmericano();

    Latte productionLatte();
}
