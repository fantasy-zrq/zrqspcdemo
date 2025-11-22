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
public class MailUserRemind implements CouponRemindDefine {
    @Override
    public void remindUser(CouponRemindDTO remindDTO) {
        log.info("采用邮件的方式进行用户提醒====");
//        throw new ClientException("模拟处理时出异常");
    }
}
