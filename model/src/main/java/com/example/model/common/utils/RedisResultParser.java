package com.example.model.common.utils;

/**
 * @author zrq
 * @time 2025/11/14 15:23
 * @description
 */
public class RedisResultParser {

    public static final Integer BITS = 13;

    //100000000000001
    public static boolean parseFirst(Long redisRes) {
        return (redisRes >> BITS) != 0;
    }

    public static Long parseSecond(Long redisRes) {
        return redisRes & ((1L << BITS) - 1);
    }

    public static void main(String[] args) {
        //8197
        boolean first = parseFirst(8197L);
        Long second = parseSecond(8197L);
        System.out.println("first = " + first);
        System.out.println("second = " + second);
    }
}
