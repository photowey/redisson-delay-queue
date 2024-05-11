package io.github.photowey.redisson.delay.queue.spring.boot.starter.config;

import io.github.photowey.redisson.delay.queue.autoconfigure.config.RedissonDelayqueueConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@code RedissonDelayqueueConfigure}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/11
 */
@Configuration
@Import(value = {
        RedissonDelayqueueConfigure.class,
})
@ConditionalOnMissingClass("org.springframework.boot.autoconfigure.AutoConfiguration")
public class RedissonDelayqueueStarterConfigure {}