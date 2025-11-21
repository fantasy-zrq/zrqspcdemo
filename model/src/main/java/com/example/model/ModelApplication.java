package com.example.model;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zrq
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.example.model.entity.mapper")
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableLogRecord(tenant = "zrq-mock-test")
public class ModelApplication {
    private final String addr = "D:\\IdeaCode\\zrqspcdemo\\tmp\\zrqTest任务推送Excel.xlsx";

    public static void main(String[] args) {
        SpringApplication.run(ModelApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            long start = System.currentTimeMillis();
            AnalysisEventListener<Object> listener = new AnalysisEventListener<>() {
                private int rowCount = 0;

                @Override
                public void invoke(Object o, AnalysisContext analysisContext) {
                    rowCount++;
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            };
            EasyExcel.read(addr, listener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("consume time = {}", end - start);
        };
    }
}
