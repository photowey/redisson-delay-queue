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
package io.github.photowey.redisson.delay.queue.autoconfigure.config;

import io.github.photowey.redisson.delay.queue.api.delayed.CompositeRedissonDelayedQueue;
import io.github.photowey.redisson.delay.queue.api.delayed.RedissonDelayedQueue;
import io.github.photowey.redisson.delay.queue.api.executor.CompositeRedissonDelayedQueueExecutor;
import io.github.photowey.redisson.delay.queue.api.executor.RedissonDelayedQueueExecutor;
import io.github.photowey.redisson.delay.queue.api.listener.CompositeRedissonDelayedQueueEventListener;
import io.github.photowey.redisson.delay.queue.api.listener.RedissonDelayedQueueBeanPostProcessor;
import io.github.photowey.redisson.delay.queue.api.manager.DefaultRedissonDelayedQueueManager;
import io.github.photowey.redisson.delay.queue.api.manager.RedissonDelayedQueueManager;
import io.github.photowey.redisson.delay.queue.api.scheduler.CompositeRedissonDelayedQueueScheduler;
import io.github.photowey.redisson.delay.queue.api.scheduler.RedissonDelayedQueueScheduler;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
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
        RedissonDelayedQueueBeanPostProcessor.class,
})
public class RedissonDelayqueueAutoConfigure {

    @Bean
    public RedissonDelayedQueueManager redissonManager(RedissonClient redisson) {
        return new DefaultRedissonDelayedQueueManager(redisson);
    }

    @Bean
    public RedissonDelayedQueueExecutor redissonExecutor() {
        return new CompositeRedissonDelayedQueueExecutor();
    }

    @Bean
    public RedissonDelayedQueue redissonDelayedQueue(RedissonDelayedQueueManager manager) {
        return new CompositeRedissonDelayedQueue(manager);
    }

    @Bean
    public RedissonDelayedQueueScheduler redissonScheduler(RedissonDelayedQueueManager manager, RedissonDelayedQueueExecutor executor) {
        return new CompositeRedissonDelayedQueueScheduler(manager, executor);
    }

    @Bean
    public CompositeRedissonDelayedQueueEventListener compositeRedissonDelayedQueueEventListener() {
        return new CompositeRedissonDelayedQueueEventListener();
    }
}