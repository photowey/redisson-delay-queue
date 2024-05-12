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
package io.github.photowey.redisson.delay.queue.core.pair;

import io.github.photowey.redisson.delay.queue.core.task.RedissonDelayedTask;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;

import java.io.Serializable;
import java.util.Objects;

/**
 * {@code QueuePair}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class QueuePair implements Serializable {

    private static final long serialVersionUID = -4388020746428084740L;

    private String topic;
    RBlockingDeque<RedissonDelayedTask<?>> blockingQueue;
    RDelayedQueue<RedissonDelayedTask<?>> delayedQueue;

    // ----------------------------------------------------------------

    public void destroy() {
        this.delayedQueue.destroy();
    }

    // ----------------------------------------------------------------

    public String topic() {
        return this.topic;
    }

    public RBlockingDeque<RedissonDelayedTask<?>> blockingQueue() {
        return this.blockingQueue;
    }

    public RDelayedQueue<RedissonDelayedTask<?>> delayedQueue() {
        return this.delayedQueue;
    }

    // ----------------------------------------------------------------

    public static QueuePairBuilder builder() {
        return new QueuePairBuilder();
    }

    public String getTopic() {
        return this.topic;
    }

    public RBlockingDeque<RedissonDelayedTask<?>> getBlockingQueue() {
        return this.blockingQueue;
    }

    public RDelayedQueue<RedissonDelayedTask<?>> getDelayedQueue() {
        return this.delayedQueue;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setBlockingQueue(RBlockingDeque<RedissonDelayedTask<?>> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void setDelayedQueue(RDelayedQueue<RedissonDelayedTask<?>> delayedQueue) {
        this.delayedQueue = delayedQueue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueuePair queuePair = (QueuePair) o;
        return Objects.equals(topic, queuePair.topic)
                && Objects.equals(blockingQueue, queuePair.blockingQueue)
                && Objects.equals(delayedQueue, queuePair.delayedQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, blockingQueue, delayedQueue);
    }

    public String toString() {
        return "QueuePair(topic=" + this.getTopic() + ", blockingQueue=" + this.getBlockingQueue() + ", delayedQueue=" + this.getDelayedQueue() + ")";
    }

    public QueuePair() {
    }

    public QueuePair(String topic, RBlockingDeque<RedissonDelayedTask<?>> blockingQueue, RDelayedQueue<RedissonDelayedTask<?>> delayedQueue) {
        this.topic = topic;
        this.blockingQueue = blockingQueue;
        this.delayedQueue = delayedQueue;
    }

    public static class QueuePairBuilder {
        private String topic;
        private RBlockingDeque<RedissonDelayedTask<?>> blockingQueue;
        private RDelayedQueue<RedissonDelayedTask<?>> delayedQueue;

        QueuePairBuilder() {
        }

        public QueuePairBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public QueuePairBuilder blockingQueue(RBlockingDeque<RedissonDelayedTask<?>> blockingQueue) {
            this.blockingQueue = blockingQueue;
            return this;
        }

        public QueuePairBuilder delayedQueue(RDelayedQueue<RedissonDelayedTask<?>> delayedQueue) {
            this.delayedQueue = delayedQueue;
            return this;
        }

        public QueuePair build() {
            return new QueuePair(this.topic, this.blockingQueue, this.delayedQueue);
        }

        public String toString() {
            return "QueuePair.QueuePairBuilder(topic=" + this.topic + ", blockingQueue=" + this.blockingQueue + ", delayedQueue=" + this.delayedQueue + ")";
        }
    }
}
