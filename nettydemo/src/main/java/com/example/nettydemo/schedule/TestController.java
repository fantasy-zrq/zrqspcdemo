package com.example.nettydemo.schedule;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zrq
 * @time 2025/8/13 10:13
 * @description
 */
@Controller
public class TestController {

    @GetMapping("/api/get")
    public void say(){
        System.out.println("");
    }
}
