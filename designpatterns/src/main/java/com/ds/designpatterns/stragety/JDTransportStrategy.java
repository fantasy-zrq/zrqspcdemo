package com.ds.designpatterns.stragety;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author zrq
 * @time 2025/11/11 11:36
 * @description
 */
public class JDTransportStrategy implements TransportStrategy {

    //小于60，按照60--0.8+10
    @Override
    public BigDecimal calculateTransport(Integer distance) {
        BigDecimal res = new BigDecimal("0");
        distance = distance < 60 ? 60 : distance;
        return res.add(new BigDecimal(distance).multiply(new BigDecimal("0.8"))).add(new BigDecimal("10"));
    }

    @Override
    public boolean isCurrent(Integer type) {
        return Objects.equals(type, 1);
    }
}
