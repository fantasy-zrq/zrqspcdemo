package com.example.model.dto.req;

import lombok.Data;

/**
 * @author zrq
 * @time 2025/8/6 21:46
 * @description
 */
@Data
public class UserRegisterReqDTO {

    private String username;

    private String password;

    private Integer age;

    //0-->女  1-->男
    private Integer sex;
}
