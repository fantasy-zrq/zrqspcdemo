package com.ds.designpatterns.stragety;

/**
 * @author zrq
 * @time 2025/11/11 11:49
 * @description
 */
public class TestStrategy {
    public static void main(String[] args) {
        BaseStrategy strategy = new BaseStrategy(new SFTransportStrategy());
        strategy.execute(20);
    }
}
