package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.common.configs.MybatisPlusBaseMetaDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/8/6 19:58
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_user")
public class UserDO extends MybatisPlusBaseMetaDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private Integer age;

    //0-->女  1-->男
    private Integer sex;
}
