package com.example.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.req.CouponCreateReqDTO;
import com.example.model.entity.CouponDO;

public interface CouponService extends IService<CouponDO> {
    void create(CouponCreateReqDTO requestParam);
}
