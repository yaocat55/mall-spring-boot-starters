package cn.net.mall.config;

import cn.net.mall.redis.RedisUtil;
import cn.net.mall.redis.TokenHelper;
import cn.net.mall.redis.UserTokenHelper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
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
 *   <li>{@link RedissonClient} — Redisson 客户端</li>
 *   <li>{@link RedissonConnectionFactory} — Redisson 连接工厂</li>
 * </ul>
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
public class RedisAutoConfiguration {

    /**
     * RedisUtil —— 基于 {@link StringRedisTemplate} 的 Redis 操作工具类 Bean。
     * <p>提供对 String 数据结构的常用操作封装，如缓存读写、分布式锁、过期设置等。</p>
     *
     * @param stringRedisTemplate Spring Boot 自动注入的 StringRedisTemplate
     * @return RedisUtil 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
    }

    /**
     * UserTokenHelper —— 基于 RedisUtil 的 JWT Token 生成与校验工具。
     * <p>条件：classpath 上存在 {@code io.jsonwebtoken.Jwts}（jjwt 依赖）。</p>
     * <p>负责用户登录 Token 的创建、刷新、校验和销毁。</p>
     *
     * @param redisUtil RedisUtil 实例
     * @return UserTokenHelper 实例
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "io.jsonwebtoken.Jwts")
    public UserTokenHelper userTokenHelper(RedisUtil redisUtil) {
        return new UserTokenHelper(redisUtil);
    }

    /**
     * TokenHelper —— 基于 RedisUtil 和 Spring Security 的认证上下文工具。
     * <p>条件：classpath 上存在 {@code org.springframework.security.core.Authentication}。</p>
     * <p>负责从请求中提取 Token、校验 Authentication 并管理安全上下文。</p>
     *
     * @param redisUtil RedisUtil 实例
     * @return TokenHelper 实例
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.security.core.Authentication")
    public TokenHelper tokenHelper(RedisUtil redisUtil) {
        return new TokenHelper(redisUtil);
    }

    /**
     * RedissonClient —— Redisson 分布式客户端。
     * <p>基于 {@link RedisProperties} 自动构建单机模式配置，支持后续扩展为集群/哨兵模式。</p>
     * <p>Bean 销毁时自动调用 {@code shutdown()} 释放连接资源。</p>
     *
     * @param redisProperties Spring Boot 自动注入的 Redis 配置属性
     * @return RedissonClient 实例
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
        singleServerConfig.setPassword(redisProperties.getPassword());
        return Redisson.create(config);
    }

    /**
     * RedissonConnectionFactory —— 基于 RedissonClient 的 Spring Data Redis 连接工厂。
     * <p>替代默认的 Lettuce/Jedis 连接工厂，使 {@link StringRedisTemplate} 底层使用 Redisson 连接池。</p>
     *
     * @param redissonClient RedissonClient 实例
     * @return RedissonConnectionFactory 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }
}
