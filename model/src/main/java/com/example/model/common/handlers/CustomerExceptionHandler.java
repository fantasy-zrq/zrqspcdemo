package com.example.model.common.handlers;

import com.example.model.common.exception.AbstractException;
import com.example.model.common.result.Result;
import com.example.model.common.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zrq
 * @time 2025/8/7 14:45
 * @description
 */
@RestControllerAdvice
@Slf4j
public class CustomerExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    public Result<?> handleAbstractException(HttpServletRequest request, AbstractException ex) {
        if (ex.getCause() != null) {
            log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString(), ex.getCause());
            return Results.failure(ex);
        }
        log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
        return Results.failure(ex);
    }
}
