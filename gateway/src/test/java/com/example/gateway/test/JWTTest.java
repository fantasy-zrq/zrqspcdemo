package com.example.gateway.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Map;

/**
 * @author zrq
 * @time 2025/8/6 17:08
 * @description
 */
public class JWTTest {
    @Test
    public void test1() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, 3);

        String token = JWT.create()
                .withExpiresAt(instance.getTime())
                .withClaim("name", "zrq")
                .withArrayClaim("hobby", new String[]{"写代码", "写程序"})
                .sign(Algorithm.HMAC256("hello"));

        System.out.println("token = " + token);
    }

    @Test
    public void test2() {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("hello")).build();

        DecodedJWT decodedJWT = verifier.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTQ3MzEzNDIsIm5hbWUiOiJ6cnEiLCJob2JieSI6WyLlhpnku6PnoIEiLCLlhpnnqIvluo8iXX0.gxKNnDY7-9rN9R2eGS_ci7GUSCzWKJ4-BYc9xwNSD3o");
//        System.out.println(decodedJWT.getClaim("name").toString());
//        List<String> hobby = decodedJWT.getClaim("hobby").asList(String.class);
//        System.out.println("hobby = " + hobby);
        Map<String, Claim> claims = decodedJWT.getClaims();
        claims.values().forEach(claim -> System.out.println(claim.asList(String.class)));
    }
}
