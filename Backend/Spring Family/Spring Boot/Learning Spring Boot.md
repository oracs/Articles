# Learning Spring Boot

修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2018.2.5 | 丁一 | 初稿 |

## 环境
### 版本
Spring Boot版本：V1.5.9

### 通过IntelliJ 创建spring boot工程
1.新建Project，选择Spring Initializer。

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

#### 通过maven命令执行spring boot工程
```bash
mvn spring-boot:run
```
#### 将应用程序打包运行
maven的pom.xml文件增加如下内容：
```java
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
在target目录下会生成xxx.jar文件。
执行和启动：
```java
java -jar xxx.jar
```

#### 在容器内运行
1.编写Dockerfile
```java
FROM openjdk:8.0.72
WORKDIR /sbtest
ADD . /sbtest
EXPOSE 9090
CMD java -jar sbtest-0.0.1-SNAPSHOT.jar
```

2.构建镜像
```java
$docker build -t sbtest:0.1 .
```
3.执行镜像
$ docker run -d -p 9090:9090 sbtest:0.1

## Spring Boot核心
### 入口类
一般在根目录下以*Aplication命名的类。通过注解：@SpringBootApplication 标示为SpringBoot应用程序。
@SpringBootApplication是一个组合注解，原子注解中有一个@EnableAutoConfiguration，自动识别配置。
如果要关闭自动配置，可以写成这样：
```java
exclude = {DataSourceAutoConfiguration.class}
```

一个典型的例子：
```java
@RestController
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SbtestApplication{

    @RequestMapping("/")  , 开始路径
    String index() {	
	}
	
	// 启动入口
	public static void main(String[] args) {
		SpringApplication.run(SbtestApplication.class, args);
	}

}
```


### 配置文件
全局配置文件：application.propertie或者application.yml。
存放位置：src/main/resources

配置文件举例：

```java
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
```java
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
```java
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
```java
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
```java
server.port=9090
```

另一个profile文件：
application-test.properties
```java
server.port=9091
```

在application.properties中，指定激活的profile:
```java
spring.profiles.active = test
```

### 自动配置
只要在pom中配置了starter的组件，spring boot会自动加载。
如果想查看spring boot的开启和关闭的spring boot组件，可以在application.properties中设置：
```java
deubg = true
```
自动配置主要是配置了注解：@EnableAutoConfiguration
它使用了@Import(EnableAutoConfigurationImportSelector.class)
其中的SpringFactoriesLoader.loadFactoryNames()会去“META-INF/spring.factories"下加载所有自动配置的组件。

#### 核心注解

@ConditionalOnBean: 当容器里有指定的Bean的条件下。
@ConditionalOnClass：当类路径下有指定的类的条件下。
@ConditionalOnExpression: 基于SpEL表达式作为判断条件。
@ConditionalOnJava: 基于JVM版本作为判断条件。
@ConditionalOnJndi: 在JNDI存在的情况下
@ConditionalOnMissingBean: 当容器里没有指定Bean的情况下。
@ConditionalOnMissingClass: 当类路径下没有指定的类的情况下。
@ConditionalOnWebApplication: 当前项目是Web应用的情况下。
@ConditionalOnProperty: 指定的属性是否有指定的值。
@ConditionalOnSingleCandidate: 当指定的Bean在容器中只有一个。


## 常用的starter介绍
### Web Starter
首先，让我们来看看 REST 服务开发。我们可以使用像 Spring MVC、Tomcat 和 Jackson 这样的库，这对于单个应用程序来说是还是存在许多依赖。

Spring Boot starter 通过添加一个依赖来帮助减少手动添加依赖的数量。 因此，不要手动指定依赖，您只需要添加一个 starter 即可，如下所示：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
现在我们可以创建一个 REST 控制器。为了简单起见，我们不会使用数据库，只专注于 REST 控制器：
```java
@RestController
public class GenericEntityController {
    private List<GenericEntity> entityList = new ArrayList<>();
 
    @RequestMapping("/entity/all")
    public List<GenericEntity> findAll() {
        return entityList;
    }
 
    @RequestMapping(value = "/entity", method = RequestMethod.POST)
    public GenericEntity addEntity(GenericEntity entity) {
        entityList.add(entity);
        return entity;
    }
 
    @RequestMapping("/entity/findby/{id}")
    public GenericEntity findById(@PathVariable Long id) {
        return entityList.stream().
                 filter(entity -> entity.getId().equals(id)).
                   findFirst().get();
    }
}
```
GenericEntity 是一个简单的 bean，id 的类型为 Long，value 为 String 类型。

就是这样，应用程序可以开始运行了，您可以访问 **http://localhost:8080/springbootapp/entity/all** 并检查控制器是否正常工作。

我们已经创建了一个配置非常少的 REST 应用程序。

### Test Starter
对于测试，我们通常使用以下组合：Spring Test、JUnit、Hamcrest 和 Mockito。我们可以手动包含所有这些库，但使用以下 Spring Boot starter 方式可以自动包含这些库：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```
请注意，您不需要指定工件的版本号。Spring Boot 会自动选择合适的版本 — 您仅需要指定 spring-boot-starter-parent-artifact 的版本。 如果之后您想要升级 Boot 库和依赖，只需在一个地方升级 Boot 版本即可，它将会处理其余部分。

让我们来测试一下之前创建的控制器。

测试控制器有两种方法：
•使用 mock 环境
•使用嵌入式 Servlet 容器（如 Tomcat 或 Jetty）

在本例中，我们将使用一个 mock 环境：
```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SpringBootApplicationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
 
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
 
    @Test
    public void givenRequestHasBeenMade_whenMeetsAllOfGivenConditions_thenCorrect() throws Exception { 
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        mockMvc.perform(MockMvcRequestBuilders.get("/entity/all")).
        andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.content().contentType(contentType)).
        andExpect(jsonPath("$", hasSize(4))); 
    } 
}
```

这里重要的是 **@WebAppConfiguration** 注解和 MockMVC 是 spring-test 模块的一部分，hasSize 是一个 Hamcrest matcher，**@Before** 是一个 JUnit 注解。这些都可以通过导入这一个这样的 starter 依赖来引入。

### Data JPA Starter
大多数 Web 应用程序都存在某些持久化 — 常见的是 JPA。

让我们使用 starter 来开始，而不是手动定义所有关联的依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

请注意，我们对这些数据库已经有了开箱即用的自动支持：H2、Derby 和 Hsqldb。在我们的示例中，我们将使用 H2。

现在让我们为实体创建仓储（repository）：
```java
public interface GenericEntityRepository extends JpaRepository<GenericEntity, Long> {}
```
现在是测试代码的时候了。这是 JUnit 测试：

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SpringBootJPATest {
     
    @Autowired
    private GenericEntityRepository genericEntityRepository;
 
    @Test
    public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
        GenericEntity genericEntity = 
          genericEntityRepository.save(new GenericEntity("test"));
        GenericEntity foundedEntity = 
          genericEntityRepository.findOne(genericEntity.getId());
         
        assertNotNull(foundedEntity);
        assertEquals(genericEntity.getValue(), foundedEntity.getValue());
    }
}
```
我们没有花时间指定数据库厂商、URL 连接和凭据。没有额外所需的配置，这些都受益于 Boot 的默认支持。 但是，如果您需要，可以进行详细配置。

### 自己写一个starter
1.在pom的关键配置项

```xml
	<groupId>com.dy</groupId>
	<artifactId>spring-boot-starter-hello</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-autoconfigure</artifactId>
		</dependency>
	</dependencies>
```

2.类型安全的属性配置类
```java
@ConfigurationProperties(prefix = "hello")
public class HelloServiceProperties {
    private String msg = "default";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

3.业务Bean
```java
public class HelloService {
    private String msg;

    public String sayHello() {
        return "hello, " + msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

4.自动配置类
```java
@Configuration
@EnableConfigurationProperties(HelloServiceProperties.class)
@ConditionalOnClass(HelloService.class)
@ConditionalOnProperty(prefix = "hello", value = "enabled", matchIfMissing = true)
public class HelloServiceAutoConfiguration {
    @Autowired
    private HelloServiceProperties helloServiceProperties;

    @Bean
    @ConditionalOnMissingBean(HelloService.class)
    public HelloService helloService() {
        HelloService helloService = helloService();
        helloService.setMsg(helloServiceProperties.getMsg());
        return helloService;
    }
}
```

5.注册配置
在src\resources目录下新建目录和文件：META-INF\spring.factories，内容如下：
```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.dy.spring_boot_starter_hello.HelloServiceAutoConfiguration
```

6.使用starter
在使用的工程中，pom做如下设置：
```java
		<dependency>
			<groupId>com.dy</groupId>
			<artifactId>spring-boot-starter-hello</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
```

application.properties文件增加配置项：
```java
hello.msg=dy111
```


Application类中调用如下：
```java

```

## Web开发

- 脚本、图片等静态资源：src/main/resources/static目录下。比如jquery，bootstrap等。
- 页面：src/main/resources/templates目录下。

spring boot默认提供了对spring mvc的集成。但如果不想使用spring mvc，则可以通过配置类实现自己的配置。
配置类的注解为：@Configuration和@EnableWebMvc
如果想使用Spring Boot的配置，由想自定义，可以继承WebMvcConfiguerAdapter类。

### 对应用服务器的支持
在application.properties文件中，
通用的容器配置型都以server作为前缀，如果是tomcat特有配置，则以server.tomcat作为前缀。

示例：
```java
server.port = # 配置程序端口号，默认8080
server.session-timeout  # 用户会话过期时间，以秒为单位
server.context-path= # 配置访问路径，默认为/

#配置tomcat
server.tomcat.uri-encoding=   # 编码方式，默认utf-8
server.tomcat.compression=  #是否开启压缩
```
#### 替换tomcat
spring boot默认使用tomcat作为引用服务器容器。 
如果想换成jetty服务器，则执行：

### 使用Angular和Bootstrap进行Web开发




```

