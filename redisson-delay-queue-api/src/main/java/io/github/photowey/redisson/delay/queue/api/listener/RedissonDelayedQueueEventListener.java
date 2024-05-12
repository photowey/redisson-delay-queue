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
package io.github.photowey.redisson.delay.queue.api.listener;

import io.github.photowey.redisson.delay.queue.api.event.RedissonDelayedTaskEvent;
import io.github.photowey.redisson.delay.queue.api.getter.ApplicationContextGetter;
import io.github.photowey.redisson.delay.queue.api.getter.RedissonClientGetter;
import io.github.photowey.redisson.delay.queue.api.getter.RedissonDelayedQueueManagerGetter;
import io.github.photowey.redisson.delay.queue.api.manager.RedissonDelayedQueueManager;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationContextAware;

/**
 * {@code RedissonDelayedQueueEventListener}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public interface RedissonDelayedQueueEventListener extends
        RedissonClientGetter, RedissonDelayedQueueManagerGetter, ApplicationContextGetter, ApplicationContextAware {

    void register(DelayedQueueEventListener listener);

    void remove(Class<DelayedQueueEventListener> clazz);

    void onEvent(RedissonDelayedTaskEvent event);

    @Override
    default RedissonClient redisson() {
        return this.applicationContext().getBean(RedissonClient.class);
    }

    @Override
    default RedissonDelayedQueueManager manager() {
        return this.applicationContext().getBean(RedissonDelayedQueueManager.class);
    }

    default boolean tryRemoveTask(String taskId) {
        return this.manager().removeTask(taskId);
    }
}
