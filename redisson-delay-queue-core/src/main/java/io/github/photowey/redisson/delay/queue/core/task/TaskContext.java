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

/**
 * {@code TaskContext}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class TaskContext<P extends Serializable> implements Serializable {

    private static final long serialVersionUID = -678970390784069991L;

    private String topic;
    private String taskId;
    private P payload;

    // ----------------------------------------------------------------

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

    public static <P extends Serializable> TaskContextBuilder<P> builder() {
        return new TaskContextBuilder();
    }

    public String getTopic() {
        return this.topic;
    }

    public String getTaskId() {
        return this.taskId;
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

    public void setPayload(P payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskContext<?> that = (TaskContext<?>) o;
        return Objects.equals(topic, that.topic)
                && Objects.equals(taskId, that.taskId)
                && Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, taskId, payload);
    }

    public String toString() {
        return "TaskContext(topic=" + this.getTopic() + ", taskId=" + this.getTaskId() + ", payload=" + this.getPayload() + ")";
    }

    public TaskContext() {}

    public TaskContext(String topic, String taskId, P payload) {
        this.topic = topic;
        this.taskId = taskId;
        this.payload = payload;
    }

    public static class TaskContextBuilder<P extends Serializable> {
        private String topic;
        private String taskId;
        private P payload;

        TaskContextBuilder() {}

        public TaskContextBuilder<P> topic(String topic) {
            this.topic = topic;
            return this;
        }

        public TaskContextBuilder<P> taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public TaskContextBuilder<P> payload(P payload) {
            this.payload = payload;
            return this;
        }

        public TaskContext<P> build() {
            return new TaskContext(this.topic, this.taskId, this.payload);
        }

        public String toString() {
            return "TaskContext.TaskContextBuilder(topic=" + this.topic + ", taskId=" + this.taskId + ", payload=" + this.payload + ")";
        }
    }
}
