package com.example.nettydemo;

import com.example.nettydemo.rpc.data.UserServiceImpl;
import com.example.nettydemo.schedule.MyTaskDemo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Future;

/**
 * @author zrq
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class NettyDemoApplication {

    private final UserServiceImpl userService;

    public static void main(String[] args) {
        SpringApplication.run(NettyDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunnableRunner(MyTaskDemo myTaskDemo) {
        return args -> myTaskDemo.runTask(() -> log.info("我是runnable任务..[{}]", Thread.currentThread().getName()));
    }

    @Bean
    public CommandLineRunner commandLineCallableRunner(MyTaskDemo myTaskDemo) {
        return args -> {
            Future<?> future = myTaskDemo.runFeature(() -> {
                log.info("我是callable任务..[{}]", Thread.currentThread().getName());
                return userService.sel("ljc");
            });
            log.info("feature值---【{}】", future.get());
        };
    }
}
