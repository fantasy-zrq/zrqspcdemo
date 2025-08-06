package com.example.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.req.UserRegisterReqDTO;
import com.example.model.entity.User;

/**
 * @author zrq
 */
public interface UserService extends IService<User> {
    void doRegister(UserRegisterReqDTO requestParam);
}
