package com.ds.newdesignpatterns.factory.abstractfactorymethod;

public interface BaseFactory {
    Americano productionAmericano();

    Latte productionLatte();
}
