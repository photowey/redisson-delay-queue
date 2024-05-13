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

import io.github.photowey.redisson.delay.queue.api.delayed.RedissonDelayedQueue;
import io.github.photowey.redisson.delay.queue.core.task.RedissonDelayedTask;
import io.github.photowey.redisson.delay.queue.spring.boot.test.App;
import io.github.photowey.redisson.delay.queue.spring.boot.test.core.counter.Counter;
import io.github.photowey.redisson.delay.queue.spring.boot.test.core.payload.HelloPayload;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.github.photowey.spring.infras.common.future.Sleepers.sleep;

/**
 * {@code RedissonDelayedQueueTest}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/13
 */
@SpringBootTest(classes = App.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedissonDelayedQueueTest {

    @Autowired
    private Counter counter;

    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext applicationContext() {
        return applicationContext;
    }

    @Test
    @Order(1)
    void testRedissonDelayedQueue_max_delayed() {
        this.counter.clean();

        RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);
        RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
                .taskId("hello.redisson.delayqueue")
                .payload("hello.redisson.delayqueue")
                // Config: 7 days
                .delayed(TimeUnit.DAYS.toMillis(8))
                .timeUnit(TimeUnit.SECONDS.name())
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            delayedQueue.offer(task);
        });

        Assertions.assertEquals(0, this.counter.registers().size());
    }

    @Test
    @Order(2)
    void testRedissonDelayedQueue_string_payload() {
        this.counter.clean();

        RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);
        for (int i = 0; i < 2; i++) {
            RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
                    .taskId(String.valueOf((i + 1)))
                    .payload(String.valueOf((i + 1)))
                    .delayed((i + 2))
                    .timeUnit(TimeUnit.SECONDS.name())
                    .build();

            delayedQueue.offer(task);
        }

        sleep(5_000);

        Assertions.assertEquals(1, this.counter.registers().size());
        Assertions.assertTrue(this.counter.registers().contains(DefaultTopicStringPayloadDelayedQueueEventListener.class.getSimpleName()));
    }

    @Test
    @Order(3)
    void testRedissonDelayedQueue_body_payload_default_topic() {
        this.counter.clean();

        RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);

        String prefix = UUID.randomUUID().toString().replaceAll("-", "");

        for (int i = 0; i < 2; i++) {
            HelloPayload payload = HelloPayload.builder()
                    .id(1760223724043808770L)
                    .name("photowey" + (i + 1))
                    .age(18 + i)
                    .build();

            RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
                    .taskId(prefix + (i + 1))
                    .payload(payload)
                    .delayed((i + 1) * 5)
                    .timeUnit(TimeUnit.SECONDS.name())
                    .build();

            delayedQueue.offer(task);
        }

        sleep(12_000);
        Assertions.assertEquals(1, this.counter.registers().size());
        Assertions.assertTrue(this.counter.registers().contains(DefaultTopicDelayedQueueEventListener.class.getSimpleName()));
    }

    @Test
    @Order(4)
    void testRedissonDelayedQueue_body_payload_topic_and_task_with_multi_listener() {
        this.counter.clean();

        RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);

        for (int i = 0; i < 2; i++) {
            HelloPayload payload = HelloPayload.builder()
                    .id(1760223724043808770L)
                    .name("photowey" + (i + 1))
                    .age(18 + i)
                    .build();

            RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
                    .topic("io.github.photowey.hello.world.delayed.query.delayqueue.topic")
                    .taskId("io.github.photowey.hello.world" + "." + (i + 1))
                    .payload(payload)
                    .delayed((i + 1) * 5)
                    .timeUnit(TimeUnit.SECONDS.name())
                    .build();

            delayedQueue.offer(task);
        }

        sleep(12_000);
        Assertions.assertEquals(2, this.counter.registers().size());
        Assertions.assertTrue(this.counter.registers().contains(Multi1DelayedQueueEventListener.class.getSimpleName()));
        Assertions.assertTrue(this.counter.registers().contains(Multi2DelayedQueueEventListener.class.getSimpleName()));
    }

    @Test
    @Order(5)
    void testRedissonDelayedQueue_body_payload_topic_and_task_with_single_listener() {
        this.counter.clean();

        RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);

        for (int i = 0; i < 2; i++) {
            HelloPayload payload = HelloPayload.builder()
                    .id(1760223724043808770L)
                    .name("photowey" + (i + 1))
                    .age(18 + i)
                    .build();

            RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
                    .topic("io.github.photowey.hello.world.delayed.query.delayqueue.topic")
                    .taskId("io.github.photowey.delayed.queue.single." + (i + 1))
                    .payload(payload)
                    .delayed((i + 1) * 5)
                    .timeUnit(TimeUnit.SECONDS.name())
                    .build();

            delayedQueue.offer(task);
        }

        sleep(12_000);
        Assertions.assertEquals(1, this.counter.registers().size());
        Assertions.assertTrue(this.counter.registers().contains(SingleDelayedQueueEventListener.class.getSimpleName()));
    }

    @Test
    @Order(6)
    void testRedissonDelayedQueue_payload_ant_multi_world_listener() {
        this.counter.clean();

        RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);

        for (int i = 0; i < 2; i++) {
            HelloPayload payload = HelloPayload.builder()
                    .id(1760223724043808770L)
                    .name("photowey" + (i + 1))
                    .age(18 + i)
                    .build();

            RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
                    .topic("io.github.photowey.hello.world.delayed.query.delayqueue.topic")
                    // io.github.photowey.ant.*
                    .taskId("io.github.photowey.ant.hello.world." + (i + 1))
                    .payload(payload)
                    .delayed((i + 1) * 5)
                    .timeUnit(TimeUnit.SECONDS.name())
                    .build();

            delayedQueue.offer(task);
        }

        sleep(12_000);
        Assertions.assertEquals(1, this.counter.registers().size());
        Assertions.assertTrue(this.counter.registers().contains(AntMultiDelayedQueueEventListener.class.getSimpleName()));
    }

    @Test
    @Order(7)
    void testRedissonDelayedQueue_payload_ant_single_world_listener() {
        this.counter.clean();

        RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);

        for (int i = 0; i < 2; i++) {
            HelloPayload payload = HelloPayload.builder()
                    .id(1760223724043808770L)
                    .name("photowey" + (i + 1))
                    .age(18 + i)
                    .build();

            RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
                    .topic("io.github.photowey.hello.world.delayed.query.delayqueue.topic")
                    // io.github.photowey.ant.#
                    .taskId("io.github.photowey.ant." + (i + 1))
                    .payload(payload)
                    .delayed((i + 1) * 5)
                    .timeUnit(TimeUnit.SECONDS.name())
                    .build();

            delayedQueue.offer(task);
        }

        sleep(12_000);
        Assertions.assertEquals(2, this.counter.registers().size());
        Assertions.assertTrue(this.counter.registers().contains(AntSingleDelayedQueueEventListener.class.getSimpleName()));
        Assertions.assertTrue(this.counter.registers().contains(AntMultiDelayedQueueEventListener.class.getSimpleName()));
    }
}