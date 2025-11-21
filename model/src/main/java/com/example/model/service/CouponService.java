package com.example.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.req.CouponCreateReqDTO;
import com.example.model.dto.req.CouponTemplateQueryReqDTO;
import com.example.model.dto.req.CouponTemplateRedeemReqDTO;
import com.example.model.dto.req.CouponTemplateRemindCreateReqDTO;
import com.example.model.dto.resp.CouponTemplateQueryRespDTO;
import com.example.model.entity.CouponDO;

public interface CouponService extends IService<CouponDO> {
    void create(CouponCreateReqDTO requestParam);

    CouponTemplateQueryRespDTO findCouponTemplate(CouponTemplateQueryReqDTO requestParam);

    void redeemUserCoupon(CouponTemplateRedeemReqDTO requestParam);

    void createCouponRemind(CouponTemplateRemindCreateReqDTO requestParam);
}
