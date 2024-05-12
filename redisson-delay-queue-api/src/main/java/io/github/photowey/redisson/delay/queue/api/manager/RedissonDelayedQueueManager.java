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
package io.github.photowey.redisson.delay.queue.api.manager;

import io.github.photowey.redisson.delay.queue.api.getter.RedissonClientGetter;
import io.github.photowey.redisson.delay.queue.api.listener.CompositeRedissonDelayedQueueEventListener;
import io.github.photowey.redisson.delay.queue.api.property.RedissonProperties;
import io.github.photowey.redisson.delay.queue.api.scheduler.RedissonDelayedQueueScheduler;
import io.github.photowey.redisson.delay.queue.core.pair.QueuePair;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.List;

/**
 * {@code RedissonDelayedQueueManager}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public interface RedissonDelayedQueueManager extends DelayedQueueManager, RedissonClientGetter, SmartInitializingSingleton {

    QueuePair registerPair(QueuePair pair);

    QueuePair tryAcquirePair(String topic);

    List<QueuePair> tryAcquirePairs();

    // ----------------------------------------------------------------

    RedissonDelayedQueueScheduler redissonScheduler();

    // ----------------------------------------------------------------
    RedissonProperties redissonProperties();

    // ----------------------------------------------------------------

    CompositeRedissonDelayedQueueEventListener redissonEventListener();
}
