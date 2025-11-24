package com.example.model.entity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ReceiveDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReceiveMapper extends BaseMapper<ReceiveDO> {
    void batchInsert(@Param("receiveCache") List<ReceiveDO> receiveCache);

    void incrementReceiveNumber(@Param("userId") Long userId, @Param("couponId") Long couponId);

    void insertOrUpdate(@Param("receiveDO") ReceiveDO receiveDO);

    void decrementReceiveNumber(@Param("userId") Long userId, @Param("couponId") Long couponId);
}
