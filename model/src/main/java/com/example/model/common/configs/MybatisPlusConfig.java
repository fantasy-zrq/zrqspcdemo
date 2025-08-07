package com.example.model.common.configs;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/8/6 20:28
 * @description
 */
@Configuration
@Slf4j
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public CustomerMetaObjectHandler customerMetaObjectHandler() {
        return new CustomerMetaObjectHandler();
    }

    static class CustomerMetaObjectHandler implements MetaObjectHandler {
        @Override
        public void insertFill(MetaObject metaObject) {
            log.info("开始插入填充...{}", new Date());
            this.strictInsertFill(metaObject, "createTime", Date::new, Date.class);
            this.strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            log.info("开始更新填充...");
            this.strictUpdateFill(metaObject, "updateTime", Date::new, Date.class);
        }
    }
}
