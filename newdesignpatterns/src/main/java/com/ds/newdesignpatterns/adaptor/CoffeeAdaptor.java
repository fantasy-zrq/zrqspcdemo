package com.ds.newdesignpatterns.adaptor;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author zrq
 * @time 2025/11/25 15:08
 * @description
 */
@Slf4j
public class CoffeeAdaptor implements SpaceUserService {
    private final CoffeeService coffeeService;

    public CoffeeAdaptor(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @Override
    public Integer getCoffee() {
        log.info("hello-->{}", "zrq");

        return Objects.equals(coffeeService.makeHotCoffee(), "hot") ? 1 : 0;
    }
}
