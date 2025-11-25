package com.ds.newdesignpatterns.observer;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zrq
 * @time 2025/11/25 20:57
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MsgEvent implements Event {

    private Long msgId;
    private String msgJson;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
