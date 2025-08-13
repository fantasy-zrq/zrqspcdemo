package com.example.nettydemo.rpc.data;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zrq
 * @time 2025/8/11 10:17
 * @description
 */
@Service
public class UserServiceImpl {

    public Map<String, Person> cache = Map.of(
            "zrq", new Person("zrq", 18),
            "ljc", new Person("ljc", 17),
            "tom", new Person("tom", 16)
    );

    public Person sel(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }
        return null;
    }
}
