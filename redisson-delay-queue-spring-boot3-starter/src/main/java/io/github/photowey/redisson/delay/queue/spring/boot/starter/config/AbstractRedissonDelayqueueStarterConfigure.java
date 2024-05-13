/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.photowey.redisson.delay.queue.spring.boot.starter.config;

import io.github.photowey.redisson.delay.queue.api.macher.DefaultDelayedAntPathMatcher;
import io.github.photowey.redisson.delay.queue.api.macher.DelayedAntPathMatcher;
import io.github.photowey.redisson.delay.queue.api.property.RedissonProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * {@code AbstractRedissonDelayqueueStarterConfigure}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/13
 */
public abstract class AbstractRedissonDelayqueueStarterConfigure {

    @Bean("io.github.photowey.redisson.delay.queue.api.property.RedissonProperties")
    public RedissonProperties redissonProperties(Environment environment) {
        return bind(environment, RedissonProperties.getPrefix(), RedissonProperties.class);
    }

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redisson(RedissonProperties redissonProperties) {
        return this.populateRedissonClient(redissonProperties);
    }

    @Bean
    @ConditionalOnMissingBean(DelayedAntPathMatcher.class)
    public DelayedAntPathMatcher delayedAntPathMatcher() {
        return new DefaultDelayedAntPathMatcher(true);
    }

    public static <T> T bind(Environment environment, String prefix, Class<T> clazz) {
        Binder binder = Binder.get(environment);

        return binder.bind(prefix, clazz).get();
    }

    private RedissonClient populateRedissonClient(RedissonProperties properties) {
        Config config = new Config();
        // Only supports single server now.
        this.populateSingleServerConfig(properties, config);

        return Redisson.create(config);
    }

    private void populateSingleServerConfig(RedissonProperties properties, Config config) {
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(properties.getAddress())
                .setDatabase(properties.getDatabase())
                .setTimeout(properties.getTimeout());

        if (StringUtils.hasText(properties.getPassword())) {
            singleServerConfig.setPassword(properties.getPassword());
        }
    }
}