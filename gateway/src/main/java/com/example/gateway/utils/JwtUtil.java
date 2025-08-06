package com.example.gateway.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * @author zrq
 * @time 2025/8/6 19:25
 * @description
 */
public final class JwtUtil {

    private static final String SIGNATURE = "x2n3y9un9c2n37984385782!*^@&#(*";

    public static String getToken(Map<String, String> map) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SIGNATURE));
    }

//    /**
//     * token异常直接报错，不需要自己定义报错逻辑
//     */
//    public static void verifyToken(String token) {
//        JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
//    }


    public static DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }
}
