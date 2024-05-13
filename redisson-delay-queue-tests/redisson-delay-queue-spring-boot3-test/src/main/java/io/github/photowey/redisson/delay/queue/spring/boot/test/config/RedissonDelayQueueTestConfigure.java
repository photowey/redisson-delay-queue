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
package io.github.photowey.redisson.delay.queue.spring.boot.test.config;

import io.github.photowey.redisson.delay.queue.spring.boot.test.core.counter.Counter;
import io.github.photowey.redisson.delay.queue.spring.boot.test.listener.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code RedissonDelayQueueTestConfigure}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/13
 */
@Configuration
public class RedissonDelayQueueTestConfigure {

    @Bean
    public Counter counter() {
        return new Counter();
    }

    @Bean
    public DefaultTopicStringPayloadDelayedQueueEventListener defaultTopicStringPayloadDelayedQueueEventListener() {
        return new DefaultTopicStringPayloadDelayedQueueEventListener();
    }

    @Bean
    public DefaultTopicDelayedQueueEventListener defaultTopicDelayedQueueEventListener() {
        return new DefaultTopicDelayedQueueEventListener();
    }

    @Bean
    public Multi1DelayedQueueEventListener multi1DelayedQueueEventListener() {
        return new Multi1DelayedQueueEventListener();
    }

    @Bean
    public Multi2DelayedQueueEventListener multi2DelayedQueueEventListener() {
        return new Multi2DelayedQueueEventListener();
    }

    @Bean
    public SingleDelayedQueueEventListener singleDelayedQueueEventListener() {
        return new SingleDelayedQueueEventListener();
    }

    @Bean
    public AntSingleDelayedQueueEventListener antSingleDelayedQueueEventListener() {
        return new AntSingleDelayedQueueEventListener();
    }

    @Bean
    public AntMultiDelayedQueueEventListener antMultiDelayedQueueEventListener() {
        return new AntMultiDelayedQueueEventListener();
    }
}