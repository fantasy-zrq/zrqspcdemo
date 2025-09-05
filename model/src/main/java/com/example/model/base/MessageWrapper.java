package com.example.model.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/9/5 10:18
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageWrapper<T> {
    private String msgId;

    private String tag;

    private String key;

    private T msg;
}
