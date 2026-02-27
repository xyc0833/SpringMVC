package com.example.controller;


import org.springframework.web.bind.annotation.*;


@RestController//直接添加注解即可
public class HelloController {

    @RequestMapping("/index/{str}")
    public String index(@PathVariable String str) {
        System.out.println(str);
        return "index";
    }

}