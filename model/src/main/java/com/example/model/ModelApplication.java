package com.example.model;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zrq
 */
@SpringBootApplication
@MapperScan("com.example.model.entity.mapper")
@EnableAspectJAutoProxy
@EnableLogRecord(tenant = "zrq-mock-test")
public class ModelApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModelApplication.class, args);
    }
}
