# mall-workid-spring-boot-starter

雪花算法分布式 ID 自动配置 Starter。基于 Redis 分配 WorkerId，支持多实例部署。

## 激活条件

- classpath 上存在 `org.springframework.data.redis.core.RedisTemplate`

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-workid-spring-boot-starter</artifactId>
</dependency>
```

### 2. 配置（可选）

```yaml
work:
  id:
    idGenerator:
      redis:
        ttl: 3600                # WorkerId 过期时间（秒）
        heartBeatIntervalSecond: 60  # 心跳续期间隔
        workerKeyDelayRemoveSecond: 60  # 关闭后延迟删除时间
```

## 注册的 Bean

| Bean | 类 | 说明 |
|------|-----|------|
| `snowFlakeIdWorker` | `SnowFlakeIdWorker` | 雪花算法 ID 生成器（纯 Java 实现） |
| `idGenerateHelper` | `IdGenerateHelper` | 分布式 ID 生成服务封装 |
| `workIdAllocator` | `WorkIdAllocator` | 基于 Redis 的 WorkerId 分配器，自动心跳续期 |

## 工作原理

```text
服务启动 → WorkIdAllocator.init()
           ↓
    读取 spring.application.name
           ↓
    在 Redis 中尝试写入 WorkerId（原子操作 SETNX）
           ↓
    写入成功 → 锁定 WorkerId，启动心跳线程
    写入失败 → 尝试下一个 WorkerId（0-1023）
           ↓
    全部被占 → 抛出异常
```

## 代码示例

```java
@Service
public class OrderService {
    private final IdGenerateHelper idGenerateHelper;

    public OrderService(IdGenerateHelper idGenerateHelper) {
        this.idGenerateHelper = idGenerateHelper;
    }

    public Order createOrder() {
        Long orderId = idGenerateHelper.nextId();
        // 使用 orderId 创建订单
    }
}
```
