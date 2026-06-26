package cn.net.mall.config;

import cn.net.mall.workid.IdGenerateHelper;
import cn.net.mall.workid.SnowFlakeIdWorker;
import cn.net.mall.workid.WorkIdAllocator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
public class WorkIdAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SnowFlakeIdWorker snowFlakeIdWorker() {
        return new SnowFlakeIdWorker(0, 0);
    }

    @Bean
    @ConditionalOnMissingBean
    public IdGenerateHelper idGenerateHelper(SnowFlakeIdWorker snowFlakeIdWorker) {
        return new IdGenerateHelper(snowFlakeIdWorker);
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkIdAllocator workIdAllocator(RedisTemplate<String, String> redisTemplate,
                                            SnowFlakeIdWorker snowFlakeIdWorker) {
        return new WorkIdAllocator(redisTemplate, snowFlakeIdWorker);
    }
}
