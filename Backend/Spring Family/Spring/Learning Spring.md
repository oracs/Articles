
修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2018.2.5 | 丁一 | 初稿 |

## 1.依赖注入
注入方式：

- xml文件
- 注解
- Java配置
- groovy配置

### 核心注解
#### 声明Bean的注解
- @Component： 声明为可被spring容器管理的组件。
- @Service: 在Service层使用。
- @Repository: 在数据访问层使用。
- @Controller: 在展现层使用。

#### 注入Bean的注解
下列注解可以在set方法或者属性上使用。

- @Autowired: Spring提供的注解
- @Inject: JSR-330提供的注解。
- @Resource: JSR-250提供的注解。

#### 配置类
- @Configuration注解声明类为配置类
- @ComponentScan注解申明扫描的Bean路径。
- @Bean，一个Java配置类中可以有0个或多个@Bean注解。

### 使用方法

- 声明Bean：通过@Component, @Service, @Controller, @Repository注解。
- 使用Bean：通过@Autoware注解。
- 声明配置类：通过@Configuration注解。
- 运行：先声明Context，然后通过context getBean。

例子：
1.编写Bean
```java
@Service
public class FunctionService {
    public String sayHello(String word){
        return "Hello " + word +" !";
    }
}
```

- @Service注解标示为Bean。

2.使用Bean
```java
public class UseFunctionService {
    @Autowired
    private FunctionService functionService;

    public String sayHello(String word){
        return functionService.sayHello(word);
    }
}
```
- @Autowired注解自动发现和注入声明的Bean。

3.配置类
```java
@Configuration
@ComponentScan("com.dy.springdemo.ci")
public class DiConfig {
}
```

- @Configuration注解声明类为配置类
- @ComponentScan注解申明扫描的Bean路径。

4.运行
```java
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(DiConfig.class);

        UseFunctionService useFunctionService = context.getBean(UseFunctionService.class);
        System.out.println(useFunctionService.sayHello("world"));

        context.close();
```

- 使用AnnotationConfigApplicationContext声明上下文。
- 通过context创建Bean。
- 调用Bean的方法。
- 关闭context

## 2.spring常用配置
### Bean的scope配置
@Scope注解支持以下类型：

- Singleton: 一个Spring容器只有一个实例，默认配置。
- Prototype: 每次调用都新建一个Bean实例。
- Request: 每一次http request都新建一个Bean实例。
- Session: 给每个http session都新建一个Bean实例。
- GlobalSession: 给每个global http session都新建一个Bean实例。
















































