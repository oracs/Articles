
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
- @Bean，一个Java配置类中可以有0个或多个@Bean注解。适用于之前遗留系统没有使用spring bean的场景，无侵入。
- @Profile, 针对不同环境下的配置

```java
@Configuration
public class ProfileConfig {
	@Bean
	@Profile("dev") //1
	public DemoBean devDemoBean() {
		return new DemoBean("from development profile");
	}

	@Bean
	@Profile("prod") //2
	public DemoBean prodDemoBean() {
		return new DemoBean("from production profile");
	}
}
```

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

```java
@Service
@Scope("prototype")
public class DemoPrototypeService {
}
```

### Bean的初始化和销毁
对于在Bean初始化之后或者销毁前需要做的事情，可以用下面两种方式支持：

- Java配置方式：@Bean的initMethod和destroyMethod
- 注解方式：@PostConstruct和@PreDestroy

```java
// 配置类
@Configuration
@ComponentScan("com.dy.springdemo.init")
public class PrePostConfig {
    @Bean(initMethod = "init",destroyMethod = "destroy")
    BeanWayService beanWayService(){
        return new BeanWayService();
    }
}

// 使用
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrePostConfig.class);
        BeanWayService beanWayService = context.getBean(BeanWayService.class);
        context.close();
```

### profile
Profile可以在不同的环境进行不同的配置设置。
通过下面3种方式支持：

- 在配置类的@Profile注解实现。
- 设定jvm的spring.profiles.active参数
- 在Servlet的context parameter中。

```java
// 配置类
@Configuration
public class ProfileConfig {
    @Bean
    @Profile("dev") //1
    public DemoBean devDemoBean() {
        return new DemoBean("from development profile");
    }

    @Bean
    @Profile("prod") //2
    public DemoBean prodDemoBean() {
        return new DemoBean("from production profile");
    }
}


// 使用
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.getEnvironment().setActiveProfiles("dev"); //1
        context.register(ProfileConfig.class);//2
        context.refresh(); //3

        DemoBean demoBean = context.getBean(DemoBean.class);

        System.out.println(demoBean.getContent());
```







































