package com.example.model.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;

import java.util.Date;
import java.util.Map;

/**
 * @author zrq
 * @time 2025/8/7 13:22
 * @description
 */
public final class JWTUtil {

    // 密钥（实际项目中建议从配置文件读取，且定期更换）
    private static final String SECRET_KEY = "9f1099fa-c9eb-44b8-8d71-5ddb016873a7";

    // 令牌过期时间（单位：毫秒），这里设置为一周小时
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 产生token默认一周时间
     *
     * @param payloadMap jwt需要添加的负载
     * @return 产生的token
     */
    public static String generateToken(Map<String, String> payloadMap) {
        return JWT.create()
                .addPayloads(payloadMap)
                .setKey(SECRET_KEY.getBytes())
                .setExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign();
    }

    /**
     * 解析jwt
     *
     * @param token 需要解析的令牌
     * @return 返回令牌的负载部分
     */
    public static Map<String, String> verifyToken(String token) {
        JSONObject payloads = JWT.of(token)
                .getPayloads();
        String jsonStr = JSONUtil.toJsonStr(payloads);
        return JSONUtil.toBean(jsonStr, Map.class);
    }
}
