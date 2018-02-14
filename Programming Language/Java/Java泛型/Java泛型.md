

```java
public <T> T getBean(Class<T> requiredType) throws BeansException {
...
}

// 调用时，直接返回同类型class对象
DemoPrototypeService bean1 = context.getBean(DemoPrototypeService.class);
```