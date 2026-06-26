package cn.net.mall.config;

import cn.net.mall.redis.RedisUtil;
import cn.net.mall.redis.TokenHelper;
import cn.net.mall.redis.UserTokenHelper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 自动配置。
 *
 * <p>条件：classpath 上存在 {@code org.springframework.data.redis.core.RedisTemplate}</p>
 *
 * <p>配置属性（来自 spring-boot-starter-data-redis）：</p>
 * <ul>
 *   <li>{@code spring.data.redis.host} (默认: localhost)</li>
 *   <li>{@code spring.data.redis.port} (默认: 6379)</li>
 *   <li>{@code spring.data.redis.password}</li>
 *   <li>{@code spring.data.redis.database} (默认: 0)</li>
 * </ul>
 *
 * <p>注册的 Bean：</p>
 * <ul>
 *   <li>{@link RedisUtil} — Redis 操作工具类</li>
 *   <li>{@link UserTokenHelper} — JWT Token 生成/校验（需要 jjwt 在 classpath 上）</li>
 *   <li>{@link TokenHelper} — Spring Security 上下文 Token 工具（需要 spring-security 在 classpath 上）</li>
 * </ul>
 *
 * <p>注意：Redisson 客户端不在本 Starter 中自动配置，各服务如有需要可在自身 pom.xml
 * 中引入 redisson-spring-boot-starter，由 Redisson 的自动配置自行处理。</p>
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "io.jsonwebtoken.Jwts")
    public UserTokenHelper userTokenHelper(RedisUtil redisUtil) {
        return new UserTokenHelper(redisUtil);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.security.core.Authentication")
    public TokenHelper tokenHelper(RedisUtil redisUtil) {
        return new TokenHelper(redisUtil);
    }
}
