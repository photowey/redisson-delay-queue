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
package io.github.photowey.redisson.delay.queue.api.executor;

import io.github.photowey.redisson.delay.queue.api.event.RedissonDelayedTaskEvent;
import io.github.photowey.redisson.delay.queue.api.getter.ApplicationContextGetter;
import io.github.photowey.redisson.delay.queue.api.manager.RedissonDelayedQueueManager;
import io.github.photowey.redisson.delay.queue.core.task.RedissonDelayedTask;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;

/**
 * {@code CompositeRedissonDelayedQueueExecutor}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class CompositeRedissonDelayedQueueExecutor implements
        RedissonDelayedQueueExecutor, ApplicationContextGetter, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public ApplicationContext applicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public RedissonDelayedQueueManager manager() {
        return this.applicationContext.getBean(RedissonDelayedQueueManager.class);
    }

    @Override
    public <P extends Serializable> void execute(RedissonDelayedTask<P> task) {
        //this.applicationContext.publishEvent(new RedissonDelayedTaskEvent(task));
        this.manager().redissonEventListener().onEvent(new RedissonDelayedTaskEvent(task));
    }
}
