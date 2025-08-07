package com.example.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.req.UserLoginReqDTO;
import com.example.model.dto.req.UserRegisterReqDTO;
import com.example.model.entity.UserDO;

/**
 * @author zrq
 */
public interface UserService extends IService<UserDO> {
    void doRegister(UserRegisterReqDTO requestParam);

    String doLogin(UserLoginReqDTO requestParam);
}
