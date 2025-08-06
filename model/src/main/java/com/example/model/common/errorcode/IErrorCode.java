package com.example.model.common.errorcode;

/**
 * @author zrq
 * @time 2025/4/5 17:19
 * @description 自定义的异常码
 */
public interface IErrorCode {

    /**
     * 错误码
     */
    String code();

    /**
     * 错误信息
     */
    String message();
}
