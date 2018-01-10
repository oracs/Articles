# Learning Spring Boot

## 环境
### 版本
Spring Boot版本：V1.5.9

### Maven配置
主要的pom.xml配置项
```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>

    <dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

通过maven命令执行spring boot工程 ：
```bash
mvn spring-boot:run
```

## Spring Boot核心
### 入口类
一般在根目录下以*Aplication命名的类。通过注解：@SpringBootApplication 标示为SpringBoot应用程序。
@SpringBootApplication是一个组合注解，原子注解中有一个@EnableAutoConfiguration，自动识别配置。
如果要关闭自动配置，可以写成这样：
```java
exclude = {DataSourceAutoConfiguration.class}
```
### 配置文件
全局配置文件：application.propertie或者application.yml。
存放位置：src/main/resources

配置文件举例：

```
server.port=9090
server.context_path=/helloboot
```

- port:指定服务的端口号
- context_path: 访问地址的根路径，和CONTEXT_PATH，contextPath通用。

### starter POM
spring boot为我们提供了大多数场景的starter pom
比如：

- spring-boot-starter-web: 对web项目开发的支持，包括Tomcat和spring mvc的支持。
- spring-boot-starter-log4j: 使用log4j日志框架。
- spring-boot-starter-Jetty: 使用Jetty作为sevlet容器。
- spring-boot-starter-test: 集成测试框架。
- spring-boot-starter-jersey: 对jersey REST的支持。
- spring-boot-starter-data-jpa: 对jpa的支持。

### 属性注入
在application.properties文件中定义属性，在程序中使用@Value注解访问。
比如，application.properties文件中定义了如下属性：
```
book.author=dy
book.name=spring boot
```

程序调用：
```java
    @Value("${book.author}")
    private String bookAuthor;

    @Value("${book.name}")
    private String bookName;
```

### 类型安全的配置
除了使用@Value方式注入属性外，Spring boot还提供了更通用的方式。可以使用@ConfigurationProperties将properties属性和Bean属性进行关联。 

1.在application.properties文件中进行属性定义：
```
author.name=dy
author.age=20
```
2.定义Bean
```java
@Component
@ConfigurationProperties(prefix = "author")
public class AuthorSetting {
    private String name;
    private Long age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
```
两个注解：
1.@ConfigurationProperties(prefix = "author")：标识为自动配置，prefix属性指定属性名的前缀。
2.@Component：标识为组件后，可以进行Bean的自动关联。


3.使用的类
```java
    @Autowired
    private AuthorSetting authorSetting;
```
这样就完成了属性的自动注入。

### 日志配置
Spring Boot支持log4j, logback, java-logging多种日志框架。
默认使用logback作为日志框架。
日志选项可以在application.properties中配置
```
logging.file=e:/tmp/log/log.log
logging.level.org.springframework.web: DEBUG
```

### Profile配置
profile可以针对不同环境进行不同的配置。
全局配置使用：application-{profile}.properties，比如application-production.properties。
然后在application.properties中设置 **spring.profiles.active = production**来指定激活的profile。

例子：
1.在resources目录下新增两个profile文件
application-dev.properties
```
server.port=9090
```

另一个profile文件：
application-test.properties
```
server.port=9091
```

在application.properties中，指定激活的profile:
```
spring.profiles.active = test
```


















```

