
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
- @EnableScheduling， 启动计划任务
- @Condition，根据满足某一个特定条件创建一个特定的Bean

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

#### 其他注解
- @Scheduled：Bean方法上使用，设置计划任务执行时间。


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

### Spring Aware
spring框架设计的初衷是让Bean对容器无感知，除了spring容器外，可以是google guice，进一步降低耦合。
但Aware组件就需要和spring容器强绑定，进而使用spring框架的特定服务。

spring提供的Aware接口列表：

| Aware接口名称 | 描述 |
|--------|--------|
| BeanNameAware | 获得容器内的Bean名称 |
| BeanFactoryAware | 获得当前bean factory, 这样可以调用容器的服务 |
| ApplicationContextAware | 获得当前的application context, 这样可以调用容器的服务 |
| MessageSourceAware | 获得message source，这样可以使用文本信息 |
| ApplicationEventPublisherAware | 应用事件发布器，可以发布事件 |
| ResourceLoaderAware  | 获得资源加载器，可以获得外部资源文件 |

```java
// bean
@Service
public class AwareService implements BeanNameAware, ResourceLoaderAware {
    private String beanName;
    private ResourceLoader loader;

    @Override
    public void setBeanName(String s) {
        this.beanName = s;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.loader = resourceLoader;
    }

    public void outputResult(){
        System.out.println("Bean的名称为：" + beanName);

        Resource resource =
                loader.getResource("classpath:com/dy/springdemo/aware/test.txt");
        try{
            System.out.println("ResourceLoader加载的文件内容为: " + IOUtils.toString(resource.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
```

### 计划任务
spring支持计划任务非常简单。

1. 在配置类中使用@EnableScheduling注解。表示启用计划任务。
1. 使用@Scheduled注解设置定时时间

```java
// Bean
public class ScheduledTaskService {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000) //1
    public void reportCurrentTime() {
        System.out.println("每隔五秒执行一次 " + dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0 28 11 ? * *"  ) //2
    public void fixTimeExecution(){
        System.out.println("在指定时间 " + dateFormat.format(new Date())+"执行");
    }
}

//配置类
@Configuration
@ComponentScan("com.dy.springdemo.init.scheduler")
@EnableScheduling
public class TaskSchedulerConfig {
}

```
### 条件注解
Spring4提供了一个更通用的基于条件的Bean的创建，即使用@Condition注解。

- 设置条件，继承Condition接口
- 每个条件下的业务Bean
- 在配置类中使用@Conditional注解设置关联条件类

```java
// 条件
public class WindowsCondition implements Condition {
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty("os.name").contains("Windows");
    }
}

// 业务Bean
public class WindowsListService implements ListService {
    @Override
    public String showListCmd() {
        return "dir";
    }
}

// 配置类
@Configuration
@ComponentScan("com.dy.springdemo.condition")
public class ConditionConifg {
    @Bean
    @Conditional(WindowsCondition.class)
    public ListService windowsListService() {
        return new WindowsListService();
    }
}
```

### 组合注解和元注解
Spring的注解本来是为了消除大量xml的复杂性，但后续的发展导致注解也越来越多。怎么办呢？Spring就提供了组合注解功能，将多个注解的功能合并，形成新的注解。

比如@Configuration就是一个组合注解，内部集成了@Component元注解。

例子：组合@Configuration 和@ComponentScan（“path”），形成一个叫@DyConfiguration的组合注解。

































