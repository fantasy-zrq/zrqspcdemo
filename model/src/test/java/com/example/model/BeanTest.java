package com.example.model;

import cn.hutool.core.bean.BeanUtil;
import com.example.model.dto.req.CouponTemplateQueryReqDTO;
import com.example.model.dto.req.CouponTemplateRedeemReqDTO;
import org.junit.jupiter.api.Test;

/**
 * @author zrq
 * @time 2025/11/19 10:47
 * @description
 */
public class BeanTest {

    @Test
    public void test1() {
        CouponTemplateRedeemReqDTO redeemReqDTO = new CouponTemplateRedeemReqDTO();
        redeemReqDTO.setCouponId(66666L);
        redeemReqDTO.setUserId(5555L);

        CouponTemplateQueryReqDTO res = BeanUtil.toBean(redeemReqDTO, CouponTemplateQueryReqDTO.class);
        System.out.println("res = " + res);
    }
}
