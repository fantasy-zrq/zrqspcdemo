package com.example.model.entity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.CouponDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author zrq
 */
public interface CouponMapper extends BaseMapper<CouponDO> {
    int decrementCouponStockByCouponId(@Param("couponId") Long couponId);
}
