package io.github.photowey.redisson.delay.queue.spring.boot.test.core.counter;

import java.util.Set;

/**
 * {@code ThreadLocalCounter}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public class ThreadLocalCounter {

    private static final ThreadLocal<Counter> COUNTER = ThreadLocal.withInitial(Counter::new);

    public static void remove() {
        COUNTER.remove();
    }

    public static void clean() {
        COUNTER.get().clean();
    }

    public static void register(String listener) {
        COUNTER.get().register(listener);
    }

    public static Set<String> registers() {
        return COUNTER.get().registers();
    }
}