package com.example.model.service.impl;

import com.example.model.dto.req.CouponRemindDTO;
import com.example.model.service.CouponRemindDefine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/11/21 22:03
 * @description
 */
@Slf4j
@Component
public class PopUpWindowUserRemind implements CouponRemindDefine {
    @Override
    public void remindUser(CouponRemindDTO remindDTO) {
        log.info("采用弹窗的方式进行用户提醒====");
    }
}
