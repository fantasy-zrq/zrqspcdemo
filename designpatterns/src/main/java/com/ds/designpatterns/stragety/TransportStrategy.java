package com.ds.designpatterns.stragety;

import java.math.BigDecimal;

/**
 * @author zrq
 */
public interface TransportStrategy {
    BigDecimal calculateTransport(Integer distance);

    boolean isCurrent(Integer type);
}
