package com.example.model.common.constance;

/**
 * @author zrq
 * @time 2025/8/6 21:20
 * @description
 */
public final class RedisConstanceKey {
    public final static String REDIS_USER_REGISTER_KEY = "zrq:spc:model:user:user-register:%s";
    public final static String REDIS_USERNAME_DUPLICATE_KEY = "zrq:spc:model:user:user-register:duplicate:%s";
    public final static String REDIS_USER_LOGIN_KEY = "zrq:spc:model:user:user-login:token:%s";
    public final static String REDIS_TASK_DISTRIBUTION_KEY = "zrq:spc:model:receive:coupon_id:%s:user_id:%s";
    public final static String REDIS_COUPON_CREATE_KEY = "zrq:spc:model:coupon:create:%s";
    public final static String REDIS_COUPON_DISTRIBUTION_KEY = "zrq:spc:model:coupon:distribution:task_id:%s:coupon_id:%s";

}
