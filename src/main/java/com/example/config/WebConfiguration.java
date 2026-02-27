package com.example.config;

import com.example.intercepter.MainInterceptor;
import com.example.intercepter.SubInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

//快速配置SpringMvc注解，如果不添加此注解会导致后续无法通过实现WebMvcConfigurer接口进行自定义配置
@EnableWebMvc
@Configuration
@ComponentScan("com.example.controller")
public class WebConfiguration implements WebMvcConfigurer {
    //我们需要使用ThymeleafViewResolver作为视图解析器，并解析我们的HTML页面
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine springTemplateEngine){
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setOrder(1);   //可以存在多个视图解析器，并且可以为他们设定解析顺序
        resolver.setCharacterEncoding("UTF-8");   //编码格式是重中之重
        resolver.setTemplateEngine(springTemplateEngine);   //和之前JavaWeb阶段一样，需要使用模板引擎进行解析，所以这里也需要设定一下模板引擎
        return resolver;
    }

    //配置模板解析器
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setSuffix(".html");   //需要解析的后缀名称
        resolver.setPrefix("classpath:");   //需要解析的HTML页面文件存放的位置，默认是webapp目录下，如果是类路径下需要添加classpath:前缀
        return resolver;
    }

    //配置模板引擎Bean
    @Bean
    public SpringTemplateEngine springTemplateEngine(ITemplateResolver resolver){
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);   //模板解析器，默认即可
        return engine;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();   //开启默认的Servlet
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //配置静态资源的访问路径
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //一号拦截器
//        registry.addInterceptor(new MainInterceptor())
//                .addPathPatterns("/**")//添加拦截器的匹配路径，只要匹配一律拦截
//                .excludePathPatterns("/home");//拦截器不进行拦截的路径

        //二号拦截器
//        registry.addInterceptor(new SubInterceptor())
//                .addPathPatterns("/**");
    }
}
