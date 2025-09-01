package com.example.model.common.bizlog;

import com.example.model.common.context.UserContext;
import com.example.model.common.enums.MockOrderTargetEnum;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.beans.Operator;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.service.IOperatorGetService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

/**
 * @author zrq
 * @time 2025/8/30 19:57
 * @description
 */
@Configuration
@Slf4j
public class LogRecordConfiguration {

    /**
     * 作为获取用户上下文对象的方法
     */
    @Bean
    public IOperatorGetService getService() {
        return () -> new Operator(UserContext.getUserName());
    }

    /**
     * 将el表达式的信息进行关系映射
     */
    @Bean
    public IParseFunction parseFunction() {
        return new IParseFunction() {
            /**
             * @return 返回值代表记录方需要使用的函数名
             */
            @Override
            public String functionName() {
                return "MOCK_ORDER";
            }

            /**
             *
             * @param value 函数入参,需要映射转换的值
             * @return 返回映射完成的值
             */
            @Override
            public String apply(Object value) {
                if (Objects.isNull(value)) {
                    throw new IllegalArgumentException("传入的格式错误");
                }
                return MockOrderTargetEnum.findOrderValueByType(((Integer) value));
            }
        };
    }

    /**
     * 将生成的log进行持久化
     */
    @Bean
    public ILogRecordService logRecordService() {

        return new ILogRecordService() {
            @Override
            public void record(LogRecord logRecord) {
                if (!"mock-order".equals(logRecord.getType())) {
                    return;
                }
                log.info("日志持久化成功--->>租户--{},日志号--{},日志操作--{},方法参数--{},dbUuid--{}",
                        logRecord.getTenant(), logRecord.getBizNo(), logRecord.getAction(), logRecord.getCodeVariable()
                        , LogRecordContext.getVariable("dbGenerateId"));
            }

            @Override
            public List<LogRecord> queryLog(String bizNo, String type) {
                return List.of();
            }

            @Override
            public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
                return List.of();
            }
        };
    }
}
