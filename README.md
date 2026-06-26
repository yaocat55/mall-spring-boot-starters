# mall-spring-boot-starters

Spring Cloud Alibaba 微服务公共组件库。将原 `mall-common` 中的通用能力拆分为独立的 Spring Boot Starter，通过 `@ConditionalOnClass` 按需加载，消除全量扫描和硬依赖。

## 模块

| 模块 | 版本 | 说明 | 必选 |
|------|:----:|------|:----:|
| `mall-cloud-bom` | 2.0.0 | 版本管控 BOM，所有服务通过 import 此 BOM 统一管理依赖版本 | ✅ |
| `mall-common-core` | 2.0.0 | 零外部依赖的基础工具类（AssertUtil、BusinessException、ApiResult 等） | ✅ |
| `mall-redis-spring-boot-starter` | 2.0.0 | Redis 工具类 + JWT Token 校验，自动配置 RedisUtil、TokenHelper | 使用 Redis 时 |
| `mall-workid-spring-boot-starter` | 2.0.0 | 雪花算法分布式 ID 生成，自动配置 WorkIdAllocator | 使用雪花 ID 时 |
| `mall-sensitive-spring-boot-starter` | 2.0.0 | 敏感词脱敏服务 | 使用敏感词时 |
| `mall-web-spring-boot-starter` | 2.0.0 | Web 通用处理：全局异常捕获、统一 API 响应包装 | Web 服务 |

## 快速使用

### 在项目中引入 BOM

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.net.mall</groupId>
            <artifactId>mall-cloud-bom</artifactId>
            <version>2.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 添加需要的 Starter

```xml
<!-- 基础工具类（几乎所有服务都需要） -->
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-common-core</artifactId>
</dependency>

<!-- Web 全局异常处理 -->
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-web-spring-boot-starter</artifactId>
</dependency>

<!-- Redis 缓存 + Token 校验 -->
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-redis-spring-boot-starter</artifactId>
</dependency>

<!-- 雪花算法分布式 ID -->
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-workid-spring-boot-starter</artifactId>
</dependency>
```

> 版本号由 `mall-cloud-bom` 统一管理，各服务不需要写版本号。

### Maven 仓库

发布在 GitHub Packages：

```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/yaocat55/mall-spring-boot-starters</url>
</repository>
```

需要配置 GitHub Personal Access Token（`packages:read` 权限）到 Maven 的 `settings.xml` 中。

## 设计原则

1. **按需引入** — 每个 Starter 有独立的 `@ConditionalOnClass`，classpath 上没有对应依赖时自动不激活
2. **最小依赖** — Starter 只声明自己需要的依赖，不传递无关 jar
3. **版本统一** — 所有模块版本由 `mall-cloud-bom` 集中管控，各服务无需关心版本号
4. **条件装配** — 通过 `AutoConfiguration.imports` 注册，Spring Boot 3.x 原生支持
