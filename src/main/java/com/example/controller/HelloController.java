package com.example.controller;

import com.example.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.http.HttpResponse;


@Controller//直接添加注解即可
public class HelloController {

    @RequestMapping(value = "/")
    public ModelAndView index(HttpServletResponse response, @CookieValue(value="JSESSIONID",required = false)String test){
        System.out.println("获取到cookie值为：" + test);
        response.addCookie(new Cookie("test","xyc01"));
        return new ModelAndView("index");  //返回ModelAndView对象，这里填入了视图的名称
    }

}