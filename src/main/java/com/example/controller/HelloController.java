package com.example.controller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Controller//直接添加注解即可
public class HelloController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @ResponseBody
    @GetMapping("/test")
    public User test(){
        return new User("Test","123456");
    }
    //文件上传
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam MultipartFile file) throws IOException {
        File fileObj = new File("test.png");
        file.transferTo(fileObj);
        System.out.println("用户上传的文件已保存到："+fileObj.getAbsolutePath());
        return "文件上传成功！";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public void download(HttpServletResponse response){
        response.setContentType("multipart/form-data");
        try(OutputStream stream = response.getOutputStream();
            InputStream inputStream = new FileInputStream("test.png")){
            IOUtils.copy(inputStream, stream);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    //将返回的内容类型设定为application/json，表示服务器端返回了一个JSON格式的数据
//    @RequestMapping(value = "/index" ,produces = "application/json")
//    public String index(){
//        JSONObject object = new JSONObject();
//        object.put("name","xyc");
//        object.put("123","123");
//        JSONArray array = new JSONArray();
//        array.add(object);
//        return object.toString();
//    }

}