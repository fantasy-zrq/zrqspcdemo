package com.example.model.common.context;

/**
 * @author zrq
 * @time 2025/8/7 15:06
 * @description
 */
public final class UserContext {

    private static final ThreadLocal<UserContextInfo> THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserInfo(UserContextInfo userInfo) {
        THREAD_LOCAL.set(userInfo);
    }

    public static String getUserName() {
        return THREAD_LOCAL.get().getUsername();
    }

    public static Integer getUserAge() {
        return THREAD_LOCAL.get().getAge();
    }

    public static Integer getUserSex() {
        return THREAD_LOCAL.get().getSex();
    }

    public static Long getFakeUserId() {
        return 1953346001616027650L;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
