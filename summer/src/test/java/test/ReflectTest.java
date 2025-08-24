package test;

import com.example.bean.User;

import java.lang.reflect.Field;

/**
 * @author zrq
 * @time 2025/8/23 16:52
 * @description
 */
public class ReflectTest {
    public static void main(String[] args) {
        User user = new User();
        Class<? extends User> userClass = user.getClass();
        String name = userClass.getSimpleName();
        System.out.println("name = " + name);
        for (Field field : userClass.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println("field.getName() = " + field.getName());
        }
    }
}
