package com.ds.designpatterns.stragety;

import java.math.BigDecimal;

/**
 * @author zrq
 * @time 2025/11/11 11:51
 * @description
 */
public class BaseStrategy {
    private TransportStrategy defaultStrategy = new YZTransportStrategy();

    public BaseStrategy(TransportStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    public BaseStrategy() {
    }

    public void execute(Integer distance) {
        BigDecimal res = defaultStrategy.calculateTransport(distance);
        System.out.println(res.toString());
    }
}
