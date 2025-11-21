package com.example.model;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
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

    @Test
    public void test2(){
        //2025-11-20 21:30:00
        long between = DateUtil.between(new Date(), new Date(2025, Calendar.DECEMBER, 20, 21, 35, 0), DateUnit.SECOND);
        System.out.println("between = " + between);
    }
}
