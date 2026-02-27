package com.example.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller//直接添加注解即可
public class HelloController {
    //@RequestMapping(value = "/index", params = {"username", "password"})
    //@RequestMapping(value = "/index" ,method = RequestMethod.POST)//直接填写访问路径
    @RequestMapping(value = "/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.getModel().put("name","helloxyc");
        return modelAndView;  //返回ModelAndView对象，这里填入了视图的名称
        //返回后会经过视图解析器进行处理
    }
    @RequestMapping(value = "/hello")
    public ModelAndView hello(@RequestParam(value ="username",required = false,defaultValue = "伞兵一号") String username, HttpSession session){
        System.out.println("接收请求参数" + username);
        System.out.println(session.getAttribute("test"));
        session.setAttribute("test","虎杖有人");
        return new ModelAndView("index");//这里的ModelandView返回的是html页面
    }
}