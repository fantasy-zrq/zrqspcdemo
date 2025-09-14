package com.example.model;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/9/13 18:45
 * @description
 */
public class DateTimeTest {
    @Test
    public void test1(){
        DateTime preOneHour = DateUtil.offsetMillisecond(new Date(), -60 * 1000 * 60);
        System.out.println("preOneHour = " + preOneHour);
    }
}
