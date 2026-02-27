package com.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@RestController//直接添加注解即可
public class HelloController {

    @RequestMapping("/index")
    public String index(){
        System.out.println("我是处理！");
        if(true) throw new RuntimeException("您的氪金力度不足，无法访问！");
        return "index";
    }

}