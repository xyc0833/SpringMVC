## @RestController

你想知道 `@RestController` 注解的核心作用，以及它和 `@Controller + @ResponseBody` 的关系——简单来说，`@RestController` 就是 `@Controller` + `@ResponseBody` 的“组合注解”，它会让控制器类中**所有处理请求的方法**默认返回 JSON/XML 等数据（而非跳转页面），无需给每个方法单独加 `@ResponseBody`。

### 1. 核心作用拆解
#### （1）基础定义
`@RestController` 是 Spring MVC 提供的**复合注解**，源码层面等价于：
```java
@Controller  // 标记这是一个控制器类，能处理请求
@ResponseBody // 让类中所有方法的返回值直接写入响应体（返回数据）
public @interface RestController {
    // ...
}
```

#### （2）核心效果
给控制器类标注 `@RestController` 后：
- 类中所有 `@GetMapping`/`@PostMapping` 等请求处理方法，**默认都会把返回值转换为 JSON/XML 格式**，直接返回给前端（比如 axios 请求能拿到数据）；
- 无需再给每个方法单独加 `@ResponseBody`（这是和 `@Controller` 最核心的区别）。

### 2. 对比示例（一看就懂）
#### 方式1：用 @Controller + 手动加 @ResponseBody（繁琐）
```java
@Controller // 仅标记控制器，不处理返回值格式
@RequestMapping("/user")
public class UserController {

    // 每个方法都要加 @ResponseBody 才能返回数据
    @GetMapping("/info")
    @ResponseBody 
    public User getUserInfo() {
        return new User("张三", 20); // 返回 JSON：{"name":"张三","age":20}
    }

    @PostMapping("/add")
    @ResponseBody 
    public String addUser() {
        return "添加成功"; // 返回字符串："添加成功"
    }
}
```

#### 方式2：用 @RestController（简洁，推荐）
```java
@RestController // 替代 @Controller + 全局生效 @ResponseBody
@RequestMapping("/user")
public class UserController {

    // 无需加 @ResponseBody，直接返回数据
    @GetMapping("/info")
    public User getUserInfo() {
        return new User("张三", 20); // 同样返回 JSON
    }

    @PostMapping("/add")
    public String addUser() {
        return "添加成功"; // 同样返回字符串
    }
}
```

### 3. 关键注意事项
1. **适用场景**：专门用于**前后端分离项目**（前端是 Vue/React 等，需要后端返回 JSON 数据）；
2. **和 @Controller 的区别**：
    - `@Controller`：默认返回**视图名称**（比如跳转到 `index.html` 页面），需要手动加 `@ResponseBody` 才返回数据；
    - `@RestController`：默认返回**数据**（JSON/XML），无法跳转页面（如果想跳转，需改用 `@Controller`）；
3. **返回值转换**：Spring 会自动把对象（如 User）转换为 JSON，依赖 `jackson-databind` 包（Spring Boot 已自动引入，Spring MVC 需手动引入）。

### 总结
1. `@RestController` 核心作用：等价于 `@Controller + @ResponseBody`，让控制器类中**所有请求方法默认返回数据（JSON/XML）**；
2. 核心优势：无需给每个方法单独加 `@ResponseBody`，简化前后端分离项目的代码；
3. 核心区别：`@Controller` 侧重跳转页面，`@RestController` 侧重返回数据。

简单说，只要你的控制器是给前端提供接口（返回数据），就用 `@RestController`；如果需要跳转页面，就用 `@Controller`。


## 编写了专门的异常处理界面 但是有问题 

类放错位置了


## 无法取到后端的数据 

用这个：
<dependency>
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>
<version>2.15.2</version>
</dependency>

你提供的 `pom.xml` 配置文件核心问题在于：**虽然引入了 FastJSON2 依赖，但没有配置让 SpringMVC 识别并使用它作为 JSON 消息转换器**，同时 JDK 版本（21）和 Spring 版本（6.0.10）的组合也需要注意兼容性。

### 配置问题分析
1. **FastJSON2 未生效**：你引入了 FastJSON2 相关依赖，但 SpringMVC 默认不会自动识别 FastJSON2，需要手动配置消息转换器，否则依然会找不到合适的 JSON 序列化方式，导致 `No acceptable representation` 异常。
2. **依赖选择问题**：Spring6 对 FastJSON2 的支持需要显式配置，相比之下 Jackson 是 Spring 官方默认集成的 JSON 库，无需额外配置即可生效，更适合新手。
3. **JDK 版本适配**：Spring 6.0.10 虽然支持 JDK 21，但部分场景下兼容性不如 JDK 17 稳定，建议优先使用 JDK 17（可选优化）。

### 修复方案（两种可选，推荐方案1）

#### 方案1：改用 Spring 官方默认的 Jackson（最简单，无需额外配置）
删除 FastJSON2 相关依赖，替换为 Jackson 依赖（Spring 会自动识别并生效）：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>SpringMVC</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>SpringMVC</name>
    <packaging>war</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- 推荐改用 JDK 17，兼容性更好 -->
        <maven.compiler.target>17</maven.compiler.target>
        <maven.compiler.source>17</maven.compiler.source>
        <junit.version>5.10.2</junit.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>5.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>6.0.10</version>
        </dependency>

        <!-- thymeleaf -->
        <dependency>
            <groupId>org.thymeleaf</groupId>
            <artifactId>thymeleaf-spring6</artifactId>
            <version>3.1.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <!-- 增加scope，避免打包冲突 -->
            <scope>provided</scope>
        </dependency>

        <!-- 替换为 Jackson（Spring 官方默认，无需额外配置） -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.15.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 方案2：保留 FastJSON2，手动配置消息转换器（需额外写配置类）
如果坚持使用 FastJSON2，除了现有依赖，还需要添加 SpringMVC 配置类来注册 FastJSON2 转换器：

1. 先保留你原有的 FastJSON2 依赖，然后创建配置类：
```java
package com.example.config;

import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    // 注册 FastJSON2 消息转换器
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 设置支持的媒体类型
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(mediaTypes);
        // 将 FastJSON2 转换器添加到最前面（优先使用）
        converters.add(0, converter);
    }
}
```

### 额外注意事项
1. **Lombok 生效配置**：如果使用 Lombok 简化 User 类（比如 `@Data` 注解），需要确保 IDE 安装了 Lombok 插件，并且 User 类写法如下（替代手动写 getter/构造器）：
```java
import lombok.Data;

@Data // 自动生成getter/setter/无参构造器/toString等
public class User {
    private String username;
    private String password;

    // 手动添加带参构造器（@Data 不会生成带参构造）
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
```
2. **依赖刷新**：修改 `pom.xml` 后，一定要点击 IDEA 中的 `Reload All Maven Projects` 按钮刷新依赖，确保新依赖下载并生效。
3. **重启服务器**：依赖生效后，重启 Tomcat 再测试 `/test` 接口。

### 总结
1. **核心问题**：FastJSON2 依赖未配置消息转换器，导致 SpringMVC 无法识别，依然找不到 JSON 序列化方式。
2. **推荐方案**：改用 Jackson 依赖（Spring 官方默认），无需额外配置即可解决 `No acceptable representation` 异常。
3. **关键操作**：修改 pom.xml 后刷新依赖 + 确保 User 类有 getter/无参构造器 + 重启服务器。

