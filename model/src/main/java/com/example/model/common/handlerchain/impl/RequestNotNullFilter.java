package com.example.model.common.handlerchain.impl;

import com.example.model.common.exception.ClientException;
import com.example.model.common.handlerchain.ChainBizMarkEnum;
import com.example.model.common.handlerchain.AbstractChainFilter;
import com.example.model.dto.req.UserLoginReqDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author zrq
 * @time 2025/8/29 11:09
 * @description
 */
@Component
public class RequestNotNullFilter implements AbstractChainFilter<UserLoginReqDTO> {

    @Override
    public void handler(UserLoginReqDTO requestParams) {
        if (Objects.isNull(requestParams.getUsername())) {
            throw new ClientException("用户名为null");
        }
        if (Objects.isNull(requestParams.getPassword())) {
            throw new ClientException("密码为null");
        }
    }

    @Override
    public String mark() {
        return ChainBizMarkEnum.USER_LOGIN_MARK.name();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
