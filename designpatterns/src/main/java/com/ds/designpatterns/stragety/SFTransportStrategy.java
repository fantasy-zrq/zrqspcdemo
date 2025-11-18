package com.ds.designpatterns.stragety;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author zrq
 * @time 2025/11/11 11:47
 * @description
 */
public class SFTransportStrategy implements TransportStrategy{
    @Override
    public BigDecimal calculateTransport(Integer distance) {
        BigDecimal res = new BigDecimal("0");
        distance = distance < 80 ? 80 : distance;
        return res.add(new BigDecimal(distance).multiply(new BigDecimal("0.8"))).add(new BigDecimal("20"));
    }

    @Override
    public boolean isCurrent(Integer type) {
        return Objects.equals(type,2);
    }
}
