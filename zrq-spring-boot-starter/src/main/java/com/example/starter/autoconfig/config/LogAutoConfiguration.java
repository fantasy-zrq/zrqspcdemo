package com.example.starter.autoconfig.config;

import com.example.starter.autoconfig.log.MyLogImpl;
import com.example.starter.autoconfig.properties.MyLogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zrq
 * @time 2025/8/28 16:17
 * @description
 */
@Configuration
@EnableConfigurationProperties(MyLogProperties.class)
public class LogAutoConfiguration {

    @Autowired
    private MyLogProperties myLogProperties;

    @Bean
    public MyLogImpl myLog() {
        MyLogImpl myLog = new MyLogImpl();
        myLog.say(myLogProperties.getName(), myLogProperties.getAge());
        return myLog;
    }
}
