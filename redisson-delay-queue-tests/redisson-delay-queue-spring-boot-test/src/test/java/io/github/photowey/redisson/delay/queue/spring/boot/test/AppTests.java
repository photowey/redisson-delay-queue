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
package io.github.photowey.redisson.delay.queue.spring.boot.test;

import io.github.photowey.redisson.delay.queue.api.property.RedissonProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * {@code AppTests}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
@SpringBootTest
class AppTests {

    @Autowired
    private RedissonProperties props;

    @Autowired
    private RedissonClient redisson;

    @Test
    void contextLoads() {}

    @Test
    void testRedissonProperties() {
        Assertions.assertEquals("redis://127.0.0.1:6379", this.props.address());
    }

    @Test
    void testRedisson() {
        String testKey = "io:github.photowey:hello:redis:cache:key";
        String testValue = "hello.redis";
        RBucket<String> bucket = this.redisson.getBucket(testKey);
        bucket.set(testValue, Duration.ofSeconds(5));
        sleep(3000);

        String testValueGet = bucket.get();
        Assertions.assertNotNull(testValueGet);
        Assertions.assertEquals(testValue, testValueGet);
        sleep(3000);
        testValueGet = bucket.get();
        Assertions.assertNull(testValueGet);
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}