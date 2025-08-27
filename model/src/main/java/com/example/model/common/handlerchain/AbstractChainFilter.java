package com.example.model.common.handlerchain;

import org.springframework.core.Ordered;

/**
 * @author zrq
 */
public interface AbstractChainFilter<T> extends Ordered {
    void handler(T requestParams);

    String mark();
}
