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


    //优惠券分发进度key
    public final static String REDIS_COUPON_DISTRIBUTION_PROCESS_KEY = "zrq:spc:model:coupon:distribution:process:task_id:%s";
    //优惠券分发用户集合key--拼接couponId不行，这个Key是redis的ZSET的key，会去重，只能放用户ID
    public final static String REDIS_COUPON_DISTRIBUTION_LIST_KEY = "zrq:spc:model:coupon:distribution:process:task_id:%s:coupon_id:%s";

    public final static String REDIS_COUPON_DISTRIBUTION_LIMIT_KEY = "zrq:spc:model:coupon:distribution:limit:user_id:";
    public final static String REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY = "zrq:spc:model:coupon:distribution:received:user_id:%s";


    //优惠券查找缓存0值Key
    public final static String REDIS_COUPON_FIND_CACHE_ZERO_KEY = "zrq:spc:model:coupon:find:zero:coupon_id:%s";
    public final static String REDIS_COUPON_FIND_LOCK_KEY = "zrq:spc:model:coupon:find:lock:coupon_id:%s";


    public final static String REDIS_COUPON_REMIND_CACHE_KEY = "zrq:spc:model:coupon:remind:lock:user_id:%s:coupon_id:%s:type:%s:remind_time:%s";
}
