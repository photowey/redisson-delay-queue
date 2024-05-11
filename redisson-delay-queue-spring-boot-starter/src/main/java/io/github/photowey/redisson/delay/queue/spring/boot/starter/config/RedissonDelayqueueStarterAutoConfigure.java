package io.github.photowey.redisson.delay.queue.spring.boot.starter.config;

import io.github.photowey.redisson.delay.queue.autoconfigure.config.RedissonDelayqueueConfigure;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@code RedissonDelayqueueAutoConfigure}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/11
 */
@Configuration
@Import(value = {
        RedissonDelayqueueConfigure.class,
})
@ConditionalOnClass(AutoConfiguration.class)
public class RedissonDelayqueueStarterAutoConfigure {}