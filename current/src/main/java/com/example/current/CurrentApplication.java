package com.example.current;

import com.example.starter.autoconfig.log.MyLogImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrentApplication {

    @Autowired
    private MyLogImpl myLog;

    public static void main(String[] args) {
        SpringApplication.run(CurrentApplication.class, args);
    }
}
