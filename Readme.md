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