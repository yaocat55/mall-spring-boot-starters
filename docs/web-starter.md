# mall-web-spring-boot-starter

Web 通用自动配置 Starter。提供全局异常处理和统一 API 响应包装。

## 激活条件

- Servlet Web 应用（`@ConditionalOnWebApplication(type = SERVLET)`）

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.net.mall</groupId>
    <artifactId>mall-web-spring-boot-starter</artifactId>
</dependency>
```

### 2. 无需额外配置，自动生效

## 注册的 Bean

### GlobalApiResultHandler

统一 API 响应格式。拦截所有 `/v1/**` 路径的 Controller 返回，包装成标准格式：

```json
{
    "code": 200,
    "message": "success",
    "data": { ... }
}
```

- 正常返回 → 自动包装为 `ApiResult.success(data)`
- 返回 `ApiResult` 类型 → 不重复包装
- 返回 `String` → 直接输出字符串
- 响应头 `INNER-REQUEST: true` 的请求 → 跳过包装（内部 Feign 调用）

### GlobalExceptionHandler

全局异常捕获，统一错误响应格式：

| 异常类型 | HTTP 状态码 | 说明 |
|---------|------------|------|
| `BusinessException` | 自定义 | 业务异常（如密码错误、资源不存在） |
| `MethodArgumentNotValidException` | 400 | 参数校验失败 |
| `HttpRequestMethodNotSupportedException` | 405 | 请求方法不支持 |
| `MissingServletRequestParameterException` | 400 | 缺少请求参数 |
| `NoHandlerFoundException` | 404 | 路由不存在 |
| `Exception`（兜底） | 500 | 未知异常 |

所有错误响应统一格式：

```json
{
    "code": 500,
    "message": "服务器内部错误"
}
```

## 代码示例

```java
// 业务异常直接抛出，由 GlobalExceptionHandler 统一处理
throw new BusinessException(HttpStatus.FORBIDDEN.value(), "权限不足");

// Controller 返回普通数据，GlobalApiResultHandler 自动包装
@GetMapping("/v1/user/info")
public UserDTO getUserInfo() {
    return userService.findById(1L);
    // 返回 → {"code":200, "message":"success", "data":{...}}
}
```
