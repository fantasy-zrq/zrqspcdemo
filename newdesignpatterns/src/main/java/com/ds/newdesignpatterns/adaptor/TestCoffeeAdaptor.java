package com.ds.newdesignpatterns.adaptor;

/**
 * @author zrq
 * @time 2025/11/25 15:10
 * @description 适配器模式，其中的CoffeeService、CoffeeServiceImpl是原来有的方法只生成热咖啡
 * 现在用户需要喝冰咖啡，那么需要新增一个用户要求的接口SpaceUserService，然后 以CoffeeAdaptor实现
 * 这个接口，重写getCoffee()方法，在这个以CoffeeAdaptor内部注入原始的CoffeeServiceImpl，获取
 * coffeeService.makeHotCoffee()的返回值，来进行更改成冰咖啡
 */
public class TestCoffeeAdaptor {
    public static void main(String[] args) {
        CoffeeService coffeeService = new CoffeeServiceImpl();
        CoffeeAdaptor adaptor = new CoffeeAdaptor(coffeeService);
        System.out.println(adaptor.getCoffee());
    }
}
