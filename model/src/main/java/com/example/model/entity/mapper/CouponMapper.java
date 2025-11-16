package com.example.model.entity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.CouponDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author zrq
 */
public interface CouponMapper extends BaseMapper<CouponDO> {
    int decrementCouponStockByCouponId(@Param("couponId") Long couponId);

    Integer decrementMySQLCouponStock(@Param("couponId") Long couponId, @Param("batchSize") Integer batchSize);

    void incrementCouponStock(@Param("couponId") Long couponId, @Param("diff") int diff);

    void updateCouponStock(@Param("couponId") Long couponId,@Param("back") int back);
}
