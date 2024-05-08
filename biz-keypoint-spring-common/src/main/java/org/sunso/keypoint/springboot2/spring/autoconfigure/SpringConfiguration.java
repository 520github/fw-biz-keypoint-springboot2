package org.sunso.keypoint.springboot2.spring.autoconfigure;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.sunso.keypoint.springboot2.spring.SpringEnvironment;
import org.sunso.keypoint.springboot2.spring.redis.RedisOperate;

@Component
@ComponentScan({"org.sunso.keypoint.springboot2.spring"})
@Configuration
public class SpringConfiguration {

    @Bean
    @ConditionalOnBean({RedisTemplate.class})
    public RedisOperate redisOperate(RedisTemplate redisTemplate) {
        return new RedisOperate(redisTemplate);
    }

    @Bean
    public SpringEnvironment springEnvironment() {
        return new SpringEnvironment();
    }
}
