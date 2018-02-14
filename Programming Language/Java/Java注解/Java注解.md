

### @Target注解
用于设定注解使用范围,通过ElementType来指定注解可使用范围的枚举集合
比如：@Target(ElementType.TYPE)

| 类别 | 描述 |
|--------|--------|
|   METHOD     |   可用于方法上     |
|  TYPE      |   可用于类或者接口上     |
|  ANNOTATION_TYPE      |   可用于注解类型上（被@interface修饰的类型）     |
|  CONSTRUCTOR      | 可用于构造方法上       |
|   FIELD     |    可用于字段上    |
|    LOCAL_VARIABLE    |    可用于局部变量上    |
|   PACKAGE     |  用于记录java文件的package信息      |
|   PARAMETER     |  可用于参数上      |

### Retention注解
注解会被保留到那个阶段. 
比如：@Retention(RetentionPolicy.RUNTIME) 

有三个值:

- RetentionPolicy.SOURCE —— 这种类型的Annotations只在源代码级别保留,编译时就会被忽略
- RetentionPolicy.CLASS —— 这种类型的Annotations编译时被保留,在class文件中存在,但JVM将会忽略
- RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的






