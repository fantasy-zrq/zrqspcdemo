package com.example.nettydemo.rpc.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zrq
 * @time 2025/8/11 10:33
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqStruct implements Serializable {
    private String className;
    private String methodName;
    private Class[] methodParamTypes;
    private Object[] methodParams;
}
