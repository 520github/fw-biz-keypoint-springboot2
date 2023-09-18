package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.autoconfigure;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.aspect.DistributeLockAspect;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler.DistributeLockHandler;
import org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler.RedissonDistributeLockHandler;

@Configuration
public class DistributeLockAutoConfiguration {

    private static final String REDISSON_PREFIX = "redis://";
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        // 1、创建配置
        Config config = new Config();
        // Redis url should start with redis:// or rediss://
        config.useSingleServer()
                .setAddress(REDISSON_PREFIX+redisHost+":"+redisPort)
                .setPassword(redisPassword)
        ;
        // 2、根据 Config 创建出 RedissonClient 实例
        return Redisson.create(config);
    }

    @Bean
    public RedissonDistributeLockHandler redissonDistributeLockHandler(RedissonClient redissonClient) {
        return new RedissonDistributeLockHandler(redissonClient);
    }

    @Bean
    public DistributeLockAspect distributeLockAspect(DistributeLockHandler distributeLockHandler) {
        return new DistributeLockAspect(distributeLockHandler);
    }
}
