# mall-common-core

基础工具类库，零外部依赖。提供所有微服务通用的工具类和异常定义。

## 使用方式

```xml
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-common-core</artifactId>
</dependency>
```

## 工具类清单

| 类 | 说明 | 关键方法 |
|-----|------|---------|
| `BusinessException` | 业务异常，携带 code + message | `new BusinessException(500, "错误信息")` |
| `AssertUtil` | 断言工具 | `isTrue(condition, msg)`、`notNull(obj, msg)`、`hasLength(str, msg)` |
| `ApiResult` | 统一响应对象 | `success(data)`、`error(code, msg)` |
| `ApiResultUtil` | ApiResult 构造辅助 | — |
| `DateFormatUtil` | 日期格式化 | `parseDate(str)`、`formatDate(date)` |
| `BigDecimalUtil` | 高精度计算 | `add(a, b)`、`subtract(a, b)`、`multiply(a, b)`、`divide(a, b)` |
| `Md5Util` | MD5 加密 | `encrypt(str)` |
| `RandomUtil` | 随机数工具 | `randomInt(max)`、`randomStr(length)` |
| `UuidUtil` | UUID 生成 | `uuid()`、`uuidWithoutHyphen()` |
| `IpUtil` | IP 地址工具 | `getIpAddress(request)` |
| `SpringUtil` | Spring 上下文工具（需 spring-context） | `getBean(clazz)`、`getProperty(key)` |
| `FillUserUtil` | 用户信息填充（需 spring-security） | `fillCreateInfo(entity)`、`fillUpdateInfo(entity)` |

## 基础实体类

| 类 | 说明 |
|-----|------|
| `BaseEntity` | 实体基类（id、createTime、updateTime、createUser、updateUser、isDel） |
| `JwtUserEntity` | JWT 用户实体（username、password、authorities） |

## 代码示例

```java
// 业务异常
if (user == null) {
    throw new BusinessException(404, "用户不存在");
}

// 断言
AssertUtil.isTrue(score > 0, "分数必须大于零");

// 响应
return ApiResult.success(userList);
return ApiResult.error(500, "系统繁忙");

// 唯一 ID
String orderNo = UuidUtil.uuidWithoutHyphen();

// 高精度计算
BigDecimal total = BigDecimalUtil.multiply(price, quantity);
```
