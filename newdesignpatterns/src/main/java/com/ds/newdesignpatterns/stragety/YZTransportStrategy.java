package com.ds.newdesignpatterns.stragety;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *  * @author zrq
 *  * @time 2025/11/11 11:47
 *  * @description
 *  */
public class YZTransportStrategy implements TransportStrategy {
    @Override
    public BigDecimal calculateTransport(Integer distance) {
        BigDecimal res = new BigDecimal("0");
        distance = distance < 50 ? 50 : distance;
        return res.add(new BigDecimal(distance).multiply(new BigDecimal("0.8"))).add(new BigDecimal("5"));
    }

    @Override
    public boolean isCurrent(Integer type) {
        return Objects.equals(type, 3);
    }
}
