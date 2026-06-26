# Getting Started

## 前置条件

- Java 17+
- Maven 3.6+
- GitHub Personal Access Token（`packages:read` 权限）

## 配置 Maven 仓库

在 `~/.m2/settings.xml` 中添加 GitHub Packages 认证：

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>yaocat55</username>
            <password>你的_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

## 引入 BOM

在项目的 `pom.xml` 中添加：

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

## 按需引入 Starter

```xml
<dependencies>
    <!-- 基础工具类（必选） -->
    <dependency>
        <groupId>cn.net.mall</groupId>
        <artifactId>mall-common-core</artifactId>
    </dependency>

    <!-- Web 全局异常处理（Web 服务推荐） -->
    <dependency>
        <groupId>cn.net.mall</groupId>
        <artifactId>mall-web-spring-boot-starter</artifactId>
    </dependency>

    <!-- Redis 缓存 + Token 校验（需要 Redis 时） -->
    <dependency>
        <groupId>cn.net.mall</groupId>
        <artifactId>mall-redis-spring-boot-starter</artifactId>
    </dependency>

    <!-- 雪花算法分布式 ID（需要分布式 ID 时） -->
    <dependency>
        <groupId>cn.net.mall</groupId>
        <artifactId>mall-workid-spring-boot-starter</artifactId>
    </dependency>

    <!-- 敏感词脱敏（需要脱敏功能时） -->
    <dependency>
        <groupId>cn.net.mall</groupId>
        <artifactId>mall-sensitive-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

> 所有版本由 `mall-cloud-bom` 统一管理，各服务不需要写 version。
