package com.example.model.common.configs;

import cn.hutool.core.bean.BeanUtil;
import com.example.model.common.context.UserContext;
import com.example.model.common.context.UserContextInfo;
import com.example.model.common.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * @author zrq
 * @time 2025/8/7 15:26
 * @description
 */
@Configuration
@RequiredArgsConstructor
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebInterceptor())
                .addPathPatterns("/api/spc/model/**")
                .excludePathPatterns("/api/spc/model/user/login", "/api/spc/model/user/register");
    }
    static class WebInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String token = request.getHeader("Authorization");
            Map<String, String> payloadMap = JWTUtil.verifyToken(token);
            UserContextInfo userInfo = BeanUtil.toBean(payloadMap, UserContextInfo.class);
            UserContext.setUserInfo(userInfo);
            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            UserContext.remove();
        }
    }
}
