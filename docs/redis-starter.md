# mall-redis-spring-boot-starter

Redis 自动配置 Starter。提供 Redis 工具类、JWT Token 生成/校验、Redisson 客户端。

## 激活条件

- classpath 上存在 `org.springframework.data.redis.core.RedisTemplate`

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-redis-spring-boot-starter</artifactId>
</dependency>
```

### 2. 配置 Redis 连接

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
      database: 0
```

### 3. 配置 JWT 密钥

```yaml
mall:
  mgt:
    tokenSecret: your_jwt_secret_key_here
    tokenExpireTimeInRecord: 3600
```

## 注册的 Bean

| Bean | 类 | 说明 |
|------|-----|------|
| `redisUtil` | `RedisUtil` | Redis 操作工具类（hash、string、自增、过期等） |
| `userTokenHelper` | `UserTokenHelper` | JWT Token 生成和验证（需要 jjwt 依赖） |
| `tokenHelper` | `TokenHelper` | 继承 UserTokenHelper，增加 Spring Security 集成（需要 spring-security） |
| `redissonClient` | `RedissonClient` | Redisson 客户端，用于分布式锁等高级功能 |
| `redissonConnectionFactory` | `RedissonConnectionFactory` | Redisson 连接工厂 |

## 配置属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `spring.data.redis.host` | String | localhost | Redis 主机 |
| `spring.data.redis.port` | int | 6379 | Redis 端口 |
| `spring.data.redis.password` | String | — | Redis 密码 |
| `spring.data.redis.database` | int | 0 | Redis 数据库索引 |
| `mall.mgt.tokenSecret` | String | 123456test | JWT 签名密钥（所有服务必须一致） |
| `mall.mgt.tokenExpireTimeInRecord` | int | 3600 | Token 过期时间（秒） |
| `mall.redis.connectionMinimumIdleSize` | int | 10 | Redisson 连接池最小空闲 |
| `mall.redis.connectionTimeout` | int | 150000 | Redisson 连接超时（毫秒） |

## 代码示例

```java
@Service
public class UserService {
    private final RedisUtil redisUtil;
    private final TokenHelper tokenHelper;

    public UserService(RedisUtil redisUtil, TokenHelper tokenHelper) {
        this.redisUtil = redisUtil;
        this.tokenHelper = tokenHelper;
    }

    public String login(String username) {
        String token = tokenHelper.generateToken(username, "{}");
        redisUtil.set("user:session:" + username, token, 3600);
        return token;
    }
}
```
