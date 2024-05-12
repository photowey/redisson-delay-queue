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
package io.github.photowey.redisson.delay.queue.api.event;

import io.github.photowey.redisson.delay.queue.core.task.RedissonDelayedTask;
import io.github.photowey.redisson.delay.queue.core.task.TaskContext;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * {@code RedissonDelayedTaskEvent}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class RedissonDelayedTaskEvent extends ApplicationEvent {

    public RedissonDelayedTaskEvent(RedissonDelayedTask<? extends Serializable> source) {
        super(source);
    }

    public <T extends Serializable> RedissonDelayedTask<T> task() {
        return (RedissonDelayedTask<T>) this.getSource();
    }

    public TaskContext<?> toTaskContext() {
        RedissonDelayedTask<Serializable> task = this.task();
        return TaskContext.builder()
                .topic(task.topic())
                .taskId(task.taskId())
                .payload(task.payload())
                .build();
    }
}
