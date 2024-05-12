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

import io.github.photowey.redisson.delay.queue.autoconfigure.config.RedissonDelayqueueAutoConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
        RedissonDelayqueueAutoConfigure.class,
})
@ConditionalOnMissingClass("org.springframework.boot.autoconfigure.AutoConfiguration")
@ConditionalOnProperty(name = "spring.redis.redisson.delayqueue.enabled", havingValue = "true", matchIfMissing = false)
public class RedissonDelayqueueStarterConfigure extends AbstractRedissonDelayqueueStarterConfigure {}