package com.example.model;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.model.common.exception.ClientException;
import com.example.model.entity.CouponDO;
import com.example.model.entity.mapper.CouponMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

/**
 * @author zrq
 * @time 2025/11/19 16:00
 * @description
 */
@Slf4j
@SpringBootTest
public class TransactionTest {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CouponMapper couponMapper;

    @Test
    public void test1() {
        CouponDO couponDO = couponMapper.selectById("50");
        if (Objects.isNull(couponDO) || Objects.equals(couponDO.getCouponStock(), 0)) {
            throw new ClientException("没这个优惠券,或者没有库存");
        }
        Object execute = transactionTemplate.execute(status -> {
            try {
                int updated = couponMapper.update(null, Wrappers.lambdaUpdate(CouponDO.class)
                        .eq(CouponDO::getCouponId, couponDO.getCouponId())
                        .set(CouponDO::getCouponStock, couponDO.getCouponStock() - 1));
                if (updated <= 0) {
                    //回滚事务
                    status.setRollbackOnly();
                    return false;
                }
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("更新出错---{}", couponDO.getCouponId());
                throw new RuntimeException(e);
            }
            return couponDO;
        });
    }

    @Test
    public void test2() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(new HikariDataSource());
        TransactionStatus status = null;
        try {
            status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            transactionManager.commit(status);
        } catch (TransactionException e) {
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }
}
