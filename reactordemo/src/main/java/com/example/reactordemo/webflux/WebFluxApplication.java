package com.example.reactordemo.webflux;

import com.alibaba.fastjson2.JSONObject;
import com.example.reactordemo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;

/**
 * @author zrq
 * @time 2025/8/6 10:49
 * @description
 */
@Slf4j
public class WebFluxApplication {
    public static void main(String[] args) {
        HttpHandler handler = (request, response) -> {
            Result result = Result.builder()
                    .code(HttpStatus.OK.value())
                    .message("请求成功！！")
                    .success(Boolean.TRUE)
                    .build();
            String jsonString = JSONObject.toJSONString(result);
            DataBuffer wrap = response.bufferFactory().wrap(jsonString.getBytes());
            System.out.println("请求URI-->" + request.getURI());
            return response.writeWith(Mono.just(wrap));
        };

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        HttpServer.create()
                .host("localhost")
                .port(8080)
                .handle(adapter)
                .bindNow();
        log.info("服务器开启...");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("服务器停止...");
    }
}
