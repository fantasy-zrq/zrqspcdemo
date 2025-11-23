package com.example.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.req.*;
import com.example.model.dto.resp.CouponRemindQueryRespDTO;
import com.example.model.dto.resp.CouponTemplateQueryRespDTO;
import com.example.model.entity.CouponDO;

import java.util.List;

public interface CouponService extends IService<CouponDO> {
    void create(CouponCreateReqDTO requestParam);

    CouponTemplateQueryRespDTO findCouponTemplate(CouponTemplateQueryReqDTO requestParam);

    void redeemUserCoupon(CouponTemplateRedeemReqDTO requestParam);

    void createCouponRemind(CouponTemplateRemindCreateReqDTO requestParam);

    void cancelCouponRemind(CouponTemplateRemindCreateReqDTO requestParam);

    List<CouponRemindQueryRespDTO> queryCouponRemind(CouponTemplateQueryRemindReqDTO requestParam);
}
