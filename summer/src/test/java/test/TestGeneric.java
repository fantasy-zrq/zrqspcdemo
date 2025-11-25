package test;

import java.lang.reflect.Method;

/**
 * @author zrq
 * @time 2025/11/25 10:01
 * @description
 */
public class TestGeneric implements Queue<Integer>{
    @Override
    public Integer pop() {
        return 1;
    }

    public static void main(String[] args) {
        for (Method method : TestGeneric.class.getDeclaredMethods()) {
            System.out.println(method.getName() + ":" + method.getReturnType().getSimpleName());
        }
    }
}

interface Queue<T extends Number> {
    T pop();
}
