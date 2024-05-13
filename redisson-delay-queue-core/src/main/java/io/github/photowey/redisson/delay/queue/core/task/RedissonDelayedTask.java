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
package io.github.photowey.redisson.delay.queue.core.task;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * {@code RedissonDelayedTask}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class RedissonDelayedTask<P extends Serializable> implements Serializable {

    private static final long serialVersionUID = 2024954306747200265L;

    private String topic;
    private String taskId;

    private long delayed;
    private String timeUnit;

    private P payload;

    // ----------------------------------------------------------------

    public void checkDelayMillis(long max) {
        long millis = this.determineTimeUnit().toMillis(this.delayed());
        if (millis > max) {
            throw new IllegalArgumentException("The delayed time is too large");
        }
    }

    // ----------------------------------------------------------------

    public TimeUnit determineTimeUnit() {
        return this.determineTimeUnit(() -> TimeUnit.MILLISECONDS);
    }

    public TimeUnit determineTimeUnit(Supplier<TimeUnit> fx) {
        if (null == timeUnit) {
            return fx.get();
        }

        return TimeUnit.valueOf(this.timeUnit);
    }

    // ----------------------------------------------------------------

    public long delayed() {
        return delayed;
    }

    public void topic(String topic) {
        this.topic = topic;
    }

    public String topic() {
        return topic;
    }

    public String taskId() {
        return taskId;
    }

    public P payload() {
        return payload;
    }

    // ----------------------------------------------------------------

    public <T> T genericPayload() {
        return (T) this.payload();
    }

    // ----------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedissonDelayedTask<?> that = (RedissonDelayedTask<?>) o;
        return delayed == that.delayed
                && Objects.equals(topic, that.topic)
                && Objects.equals(taskId, that.taskId)
                && Objects.equals(timeUnit, that.timeUnit)
                && Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, taskId, delayed, timeUnit, payload);
    }

    // ----------------------------------------------------------------

    public static <P extends Serializable> RedissonDelayedTaskBuilder<P> builder() {
        return new RedissonDelayedTaskBuilder();
    }

    public String getTopic() {
        return this.topic;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public long getDelayed() {
        return this.delayed;
    }

    public String getTimeUnit() {
        return this.timeUnit;
    }

    public P getPayload() {
        return this.payload;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setDelayed(long delayed) {
        this.delayed = delayed;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

    public RedissonDelayedTask() {}

    public RedissonDelayedTask(String topic, String taskId, long delayed, String timeUnit, P payload) {
        this.topic = topic;
        this.taskId = taskId;
        this.delayed = delayed;
        this.timeUnit = timeUnit;
        this.payload = payload;
    }

    public static class RedissonDelayedTaskBuilder<P extends Serializable> {
        private String topic;
        private String taskId;
        private long delayed;
        private String timeUnit;
        private P payload;

        RedissonDelayedTaskBuilder() {
        }

        public RedissonDelayedTaskBuilder<P> topic(String topic) {
            this.topic = topic;
            return this;
        }

        public RedissonDelayedTaskBuilder<P> taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public RedissonDelayedTaskBuilder<P> delayed(long delayed) {
            this.delayed = delayed;
            return this;
        }

        public RedissonDelayedTaskBuilder<P> timeUnit(String timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public RedissonDelayedTaskBuilder<P> payload(P payload) {
            this.payload = payload;
            return this;
        }

        public RedissonDelayedTask<P> build() {
            return new RedissonDelayedTask(this.topic, this.taskId, this.delayed, this.timeUnit, this.payload);
        }

        public String toString() {
            return "RedissonDelayedTask.RedissonDelayedTaskBuilder(topic=" + this.topic + ", taskId=" + this.taskId + ", delayed=" + this.delayed + ", timeUnit=" + this.timeUnit + ", payload=" + this.payload + ")";
        }
    }
}


