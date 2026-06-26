package cn.net.mall.config;

import cn.net.mall.sensitive.CustomMaskService;
import cn.net.mall.sensitive.ICustomMaskService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SensitiveAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ICustomMaskService customMaskService() {
        return new CustomMaskService();
    }
}
