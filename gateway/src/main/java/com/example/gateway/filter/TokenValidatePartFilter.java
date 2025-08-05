package com.example.gateway.filter;

import com.alibaba.fastjson2.JSONObject;
import com.example.gateway.config.WhitePathConfig;
import com.example.gateway.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author zrq
 * @time 2025/8/5 14:40
 * @description
 */
@Component
@RequiredArgsConstructor
public class TokenValidatePartFilter extends AbstractGatewayFilterFactory<WhitePathConfig> {

    @Override
    public GatewayFilter apply(WhitePathConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            RequestPath requestPath = request.getPath();
            if (!isWhitePath(config, requestPath.toString())) {
                String username = request.getHeaders().getFirst("username");
                String token = request.getHeaders().getFirst("token");
                if (!("zrq".equals(username) && "qwerty".equals(token))) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    Result result = Result.builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message("fail")
                            .success(Boolean.FALSE)
                            .build();
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(JSONObject.toJSONString(result).getBytes());
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                }
            }
            return chain.filter(exchange);
        };
    }

    private Boolean isWhitePath(WhitePathConfig config, String requestPath) {
        return requestPath != null && config.getWhitePathList().contains(requestPath);
    }
}
