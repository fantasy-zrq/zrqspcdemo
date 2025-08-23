package test;

import com.example.bean.User;

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
    }
}
