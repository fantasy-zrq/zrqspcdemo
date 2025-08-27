package com.example.model.common.handlerchain.impl;

import com.example.model.common.exception.ClientException;
import com.example.model.common.handlerchain.ChainBizMarkEnum;
import com.example.model.common.handlerchain.AbstractChainFilter;
import com.example.model.dto.req.UserLoginReqDTO;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/8/29 11:09
 * @description
 */
@Component
public class RequestCheckLengthFilter implements AbstractChainFilter<UserLoginReqDTO> {
    @Override
    public void handler(UserLoginReqDTO requestParams) {
        if (requestParams.getUsername().length() > 3) {
            throw new ClientException("用户名长度不符");
        }
    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.USER_LOGIN_MARK.name();
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
