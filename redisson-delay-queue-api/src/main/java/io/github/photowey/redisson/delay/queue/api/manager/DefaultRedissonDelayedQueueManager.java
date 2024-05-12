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

import io.github.photowey.redisson.delay.queue.api.delayed.DelayedQueue;
import io.github.photowey.redisson.delay.queue.api.listener.CompositeRedissonDelayedQueueEventListener;
import io.github.photowey.redisson.delay.queue.api.property.RedissonProperties;
import io.github.photowey.redisson.delay.queue.api.scheduler.RedissonDelayedQueueScheduler;
import io.github.photowey.redisson.delay.queue.core.pair.QueuePair;
import io.github.photowey.redisson.delay.queue.core.task.RedissonDelayedTask;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code DefaultRedissonDelayedQueueManager}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class DefaultRedissonDelayedQueueManager implements RedissonDelayedQueueManager {

    // ----------------------------------------------------------------

    private final ConcurrentHashMap<String, DelayedQueue> ctx = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, QueuePair> pairs = new ConcurrentHashMap<>();

    private final RedissonClient redisson;

    private ConfigurableListableBeanFactory beanFactory;

    public DefaultRedissonDelayedQueueManager(RedissonClient redisson) {
        this.redisson = redisson;
    }

    // ----------------------------------------------------------------

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    // ----------------------------------------------------------------

    @Override
    public void afterSingletonsInstantiated() {
        this.init();
        this.redissonScheduler().start();
    }

    public void init() {
        Set<String> topics = this.redissonProperties().getDelayed().topics();

        for (String topicx : topics) {
            RBlockingDeque<RedissonDelayedTask<?>> blockingQueue = this.redisson().getBlockingDeque(topicx);
            RDelayedQueue<RedissonDelayedTask<?>> delayedQueue = this.redisson().getDelayedQueue(blockingQueue);

            QueuePair pair = QueuePair.builder()
                    .topic(topicx)
                    .blockingQueue(blockingQueue)
                    .delayedQueue(delayedQueue)
                    .build();

            this.registerPair(pair);
        }
    }

    // ----------------------------------------------------------------

    @Override
    public RedissonClient redisson() {
        return this.redisson;
    }

    // ----------------------------------------------------------------

    @Override
    public DelayedQueue register(DelayedQueue delayedQueue) {
        String topic = delayedQueue.topic();
        DelayedQueue image = this.ctx.get(topic);
        if (null == image) {
            this.ctx.computeIfAbsent(topic, (x) -> delayedQueue);
        }

        return image;
    }

    @Override
    public DelayedQueue tryAcquire(String topic) {
        return this.ctx.get(topic);
    }

    @Override
    public boolean topicContains(String topic) {
        if (this.ctx.containsKey(topic)) {
            return true;
        }

        return this.determineIsTopicContains(topic);
    }

    @Override
    public boolean taskContains(String taskId) {
        return this.determineIsTaskContains(taskId);
    }

    @Override
    public boolean removeTask(String taskId) {
        return this.tryRemoveTask(taskId);
    }

    // ----------------------------------------------------------------

    @Override
    public QueuePair registerPair(QueuePair pair) {
        String topic = pair.topic();

        QueuePair image = this.pairs.get(topic);
        if (null == image) {
            return this.pairs.computeIfAbsent(topic, (x) -> {
                this.registerTopic(topic);

                return pair;
            });
        }

        return image;
    }

    @Override
    public QueuePair tryAcquirePair(String topic) {
        return this.pairs.get(topic);
    }

    @Override
    public List<QueuePair> tryAcquirePairs() {
        return Collections.unmodifiableList(new ArrayList<>(this.pairs.values()));
    }

    @Override
    public RedissonDelayedQueueScheduler redissonScheduler() {
        return this.beanFactory.getBean(RedissonDelayedQueueScheduler.class);
    }

    @Override
    public RedissonProperties redissonProperties() {
        return this.beanFactory.getBean(RedissonProperties.class);
    }

    @Override
    public CompositeRedissonDelayedQueueEventListener redissonEventListener() {
        return this.beanFactory.getBean(CompositeRedissonDelayedQueueEventListener.class);
    }

    // ----------------------------------------------------------------

    private void registerTopic(String topic) {
        String topicSet = this.redissonProperties().delayed().registry().topicSet();
        this.redisson().getSetCache(topicSet).add(topic);
    }

    private boolean determineIsTopicContains(String topic) {
        String topicSet = this.redissonProperties().delayed().registry().topicSet();
        return this.redisson().getSetCache(topicSet).contains(topic);
    }

    private boolean determineIsTaskContains(String taskId) {
        String taskSet = this.redissonProperties().delayed().registry().taskSet();
        return this.redisson().getSetCache(taskSet).contains(taskId);
    }

    private boolean tryRemoveTask(String taskId) {
        String taskSet = this.redissonProperties().delayed().registry().taskSet();
        return this.redisson().getSetCache(taskSet).remove(taskId);
    }

    // ----------------------------------------------------------------

}