package com.example.model;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author zrq
 * @time 2025/11/17 16:21
 * @description
 */
public class JsonTest {
    @Test
    public void test1() {
        TestJson object = new TestJson("zrq",18,true);
        String res = JSON.toJSONString(object);
        System.out.println("res = " + res);
    }
}

@Data
@AllArgsConstructor
class TestJson {

    public String name;
    public Integer age;
    public boolean success;

    public Boolean isError() {
        return !Objects.equals(name,"zrq");
    }
}

@Getter
@RequiredArgsConstructor
enum TestEnum {
    yes(1),
    no(0);

    public final int status;

    public boolean isSuccess(String enumStatus) {
        return Objects.equals(Integer.parseInt(enumStatus), yes.status);
    }
}
