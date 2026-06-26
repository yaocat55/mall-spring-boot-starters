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

@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
public class RedisAutoConfiguration {

    /**
     * RedisUtil - 基于 StringRedisTemplate 自动配置
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
    }

    /**
     * UserTokenHelper - 基于 RedisUtil 自动配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "io.jsonwebtoken.Jwts")
    public UserTokenHelper userTokenHelper(RedisUtil redisUtil) {
        return new UserTokenHelper(redisUtil);
    }

    /**
     * TokenHelper - 依赖 Spring Security，当类路径中不存在 Authentication 时自动跳过
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.security.core.Authentication")
    public TokenHelper tokenHelper(RedisUtil redisUtil) {
        return new TokenHelper(redisUtil);
    }

    /**
     * Redisson 客户端
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

    @Bean
    @ConditionalOnMissingBean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }
}
