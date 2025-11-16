package com.example.model.entity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.CouponDistributionFailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zrq
 */
public interface CouponDistributionFailMapper extends BaseMapper<CouponDistributionFailDO> {
    void batchInsert(@Param("failList") List<CouponDistributionFailDO> failList);
}
