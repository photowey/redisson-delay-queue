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
import io.github.photowey.redisson.delay.queue.core.task.TaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code CompositeRedissonDelayedQueueEventListener}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class CompositeRedissonDelayedQueueEventListener implements RedissonDelayedQueueEventListener {

    public final ConcurrentHashMap<String, DelayedQueueEventListener> ctx = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    private final Logger log = LoggerFactory.getLogger(CompositeRedissonDelayedQueueEventListener.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public ApplicationContext applicationContext() {
        return this.applicationContext;
    }

    @Override
    public void register(DelayedQueueEventListener listener) {
        this.ctx.computeIfAbsent(listener.getClass().getName(), (x) -> listener);
    }

    @Override
    public void remove(Class<DelayedQueueEventListener> clazz) {
        this.ctx.remove(clazz.getName());
    }

    @Override
    public void onEvent(RedissonDelayedTaskEvent event) {
        TaskContext<?> ctx = event.toTaskContext();

        List<DelayedQueueEventListener> eventListeners = new ArrayList<>(this.ctx.values());
        AnnotationAwareOrderComparator.sort(eventListeners);

        for (DelayedQueueEventListener eventListener : eventListeners) {
            if (eventListener.supports(ctx)) {
                this.handleEvent(eventListener, ctx);
            }
        }

        this.tryRemoveTask(ctx.taskId());
    }

    private void handleEvent(DelayedQueueEventListener eventListener, TaskContext<?> ctx) {
        try {
            eventListener.handle(ctx);
        } catch (Exception e) {
            log.error("redisson.delayqueue: handle.delayed.task.failed,report:[listener:{},topic:{},taskId:{}]",
                    eventListener.getClass().getName(), ctx.taskId(), ctx.taskId());
        }
    }
}
