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
package io.github.photowey.redisson.delay.queue.spring.boot.test.listener;

import io.github.photowey.redisson.delay.queue.api.listener.AbstractAntDelayedQueueEventListener;
import io.github.photowey.redisson.delay.queue.core.task.TaskContext;
import io.github.photowey.redisson.delay.queue.spring.boot.test.core.counter.Counter;
import io.github.photowey.redisson.delay.queue.spring.boot.test.core.payload.HelloPayload;
import io.github.photowey.spring.infras.common.json.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

/**
 * {@code AntSingleDelayedQueueEventListener}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/13
 */
@Slf4j
public class AntSingleDelayedQueueEventListener extends AbstractAntDelayedQueueEventListener {

    @Autowired
    private Counter counter;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 300;
    }

    @Override
    public boolean supports(TaskContext<?> ctx) {
        return this.matches("io.github.photowey:hello:world:*", ctx.topic())
                && this.matches("io.github.photowey.ant.#", ctx.taskId());
    }

    @Override
    public void handle(TaskContext<?> ctx) {
        this.counter.register(this.getClass().getSimpleName());

        HelloPayload payload = (HelloPayload) ctx.getPayload();
        log.info("json.generic: ant.single.listener:[{}:{}]", ctx.taskId(), JSON.Jackson.toJSONString(payload));
    }
}