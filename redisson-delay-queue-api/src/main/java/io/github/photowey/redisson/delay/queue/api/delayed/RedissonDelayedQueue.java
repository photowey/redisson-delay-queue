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
package io.github.photowey.redisson.delay.queue.api.delayed;

import io.github.photowey.redisson.delay.queue.api.manager.RedissonDelayedQueueManager;
import io.github.photowey.redisson.delay.queue.api.property.RedissonProperties;
import io.github.photowey.redisson.delay.queue.core.task.RedissonDelayedTask;
import org.redisson.api.RedissonClient;

import java.io.Serializable;

/**
 * {@code RedissonDelayedQueue}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public interface RedissonDelayedQueue extends DelayedQueue {

    RedissonDelayedQueueManager manager();

    default RedissonClient redisson() {
        return this.manager().redisson();
    }

    // ----------------------------------------------------------------

    RedissonProperties redissonProperties();

    // ----------------------------------------------------------------

    void registerTask(String taskId);

    // ----------------------------------------------------------------

    <P extends Serializable> void offer(RedissonDelayedTask<P> task);
}
