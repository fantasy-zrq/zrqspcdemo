package com.example.gateway.filter;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gateway.config.WhitePathConfig;
import com.example.gateway.result.Result;
import com.example.gateway.utils.JwtUtil;
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
public class TokenValidateGatewayFilterFactory extends AbstractGatewayFilterFactory<WhitePathConfig> {

    //必须手动将Config文件传给父类
    public TokenValidateGatewayFilterFactory() {
        super(WhitePathConfig.class);
    }

    /**
     * 使用JWT重构网关层逻辑
     */
    @Override
    public GatewayFilter apply(WhitePathConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            RequestPath requestPath = request.getPath();
            if (!isWhitePath(config, requestPath.toString())) {
                String token = request.getHeaders().getFirst("token");
                DecodedJWT decodedJWT = JwtUtil.getTokenInfo(token);
                String username = decodedJWT.getClaim("username").toString();
                String userId = decodedJWT.getClaim("userId").toString();
                if (!("zrq".equals(username) && "firstOne".equals(userId))) {
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
