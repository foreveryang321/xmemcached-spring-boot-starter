# xmemcached-spring-boot-starter
Spring Boot Starter for XMemcached

## 特性
- spring-boot-starter
- 全局默认过期时间配置
- 自定重连
- 自定义过期时间注解 `Expired` 支持 SpEl 表达式动态配置时间


## Getting Started
> pom.xml
```xml
<dependency>
    <groupId>top.ylonline</groupId>
    <artifactId>xmemcached-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

> application.yml
```yaml
xmemcached:
    address: 127.0.0.1:12200
    expire: 60
    allow-null-values: false
    command-factory: binary
    session-locator: ketam
    socket-options:
      tcp-no-delay: true
      keep-alive: true
```
