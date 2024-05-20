# redisson-delay-queue

A distributed delay queue based on Redisson.

[Spring Boot example](https://github.com/photowey/redisson-delay-queue-spring-boot-example)

[Spring Boot V3 example](https://github.com/photowey/redisson-delay-queue-spring-boot3-example)

## 1.`Usage`

Add this to your `pom.xml`

### 1.1.`Spring Boot`

```xml
<!-- ${redisson-delay-queue.version} == ${latest.version} -->
<!-- https://central.sonatype.com/artifact/io.github.photowey/redisson-delay-queue/versions -->
<dependency>
    <groupId>io.github.photowey</groupId>
    <artifactId>redisson-delay-queue-spring-boot-starter</artifactId>
    <version>${redisson-delay-queue.version}</version>
</dependency>
```

### 1.2.`Spring Boot V3`

```xml

<dependency>
    <!-- // ... -->
    <artifactId>redisson-delay-queue-spring-boot3-starter</artifactId>
    <!-- // ... -->
</dependency>
```

## 2.`APIs`

### 2.1.`Configuration`

#### 2.1.1.`Yaml`

```yml
spring:
  redis:
    # ...
    redisson:
      delayqueue:
        enabled: true
        mode: SINGLE
        address: "redis://${local.config.redis.host}:${local.config.redis.port}"
        password: "${local.config.redis.password}"
        master: "master"
        database: 0
        timeout: 10_000
        delayed:
          # 7 DAYS
          max: 604_800_000
          # Default topic.
          # Default value: io.github.photowey.global.redisson.delayqueue.topic
          topic: "io.github.photowey.global.redisson.delayqueue.topic"
          # Custom topics, if necessary.
          topics:
            - "io.github.photowey.hello.world.delayed.query.delayqueue.topic"
          # Scheduled executor config.
          ticker:
            initial-delay: 0
            period: 5
            unit: "SECONDS"
          # poll task timeout config.
          poll:
            timeout: 2
            unit: "SECONDS"
          # Global registry cache-key.
          registry:
            # Topics
            topic-set: "io:github:photowey:global:redisson:delayqueue:topicset"
            # TaskIds
            task-set: "io:github:photowey:global:redisson:delayqueue:taskset"
```

#### 2.1.2.`RedissonClient`

```java

@Bean
@ConditionalOnMissingBean(RedissonClient.class)
public RedissonClient redisson(RedissonProperties redissonProperties) {
    // Currently only single node is supported, via auto-configuration.
    return this.populateRedissonClient(redissonProperties);
}
```

### 2.2.`RedissonDelayedQueue`

#### 2.2.1.`ApplicationContext`

```java
RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);
```

#### 2.2.2.`DI`

```java

@Autowired
private RedissonDelayedQueue delayedQueue

// ...
```

### 2.3.`Publish`

#### 2.3.1.`Default Topic`

```java
RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);
for(
int i = 0;
i< 2;i++){
RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
        .taskId(String.valueOf((i + 1)))
        .payload(String.valueOf((i + 1))) // String payload
        .delayed((i + 2))
        .timeUnit(TimeUnit.SECONDS.name())
        .build();

    delayedQueue.

offer(task);
}
```

#### 2.3.3.`Custom Topic`

```java
RedissonDelayedQueue delayedQueue = this.applicationContext().getBean(RedissonDelayedQueue.class);

for(
int i = 0;
i< 2;i++){
HelloPayload payload = HelloPayload.builder()
        .id(1760223724043808770L)
        .name("photowey" + (i + 1))
        .age(18 + i)
        .build();

RedissonDelayedTask<Serializable> task = RedissonDelayedTask.builder()
        // Custom topic
        .topic("io.github.photowey.hello.world.delayed.query.delayqueue.topic")
        // TaskId
        .taskId("io.github.photowey.hello.world" + "." + (i + 1))
        // Body payload
        .payload(payload)
        .delayed((i + 1) * 5)
        .timeUnit(TimeUnit.SECONDS.name())
        .build();

    delayedQueue.

offer(task);
}
```

### 2.4.`Consume`

> Add custom listener implements `DelayedQueueEventListener`

#### 2.4.1.`Custom pattern`

```java

@Slf4j
public class Multi1DelayedQueueEventListener implements DelayedQueueEventListener {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    @Override
    public boolean supports(TaskContext<?> ctx) {
        // Filter topic or task.
        return ctx.topic().startsWith("io.github.photowey.hello.world")
                && ctx.taskId().startsWith("io.github.photowey.hello.world");
    }

    @Override
    public void handle(TaskContext<?> ctx) {
        // ...
    }
}
```

#### 2.4.2.`And Pattern`

> Add custom listener extends `AbstractAntDelayedQueueEventListener`

```java

@Slf4j
public class AntMultiDelayedQueueEventListener extends AbstractAntDelayedQueueEventListener {

    @Autowired
    private Counter counter;

    @Override
    public int getOrder() {
        // The sorting order of Listener.
        return Ordered.HIGHEST_PRECEDENCE + 300;
    }

    @Override
    public boolean supports(TaskContext<?> ctx) {
        // Matches
        return this.matches("io.github.photowey:hello:world:*", ctx.topic())
                && this.matches("io.github.photowey.ant.*", ctx.taskId());
    }

    @Override
    public void handle(TaskContext<?> ctx) {
        // ...
    }
}
```

```java
/**
 * Determines if the given path matches the specified pattern.
 *
 * This method first converts the given pattern and path to the Ant expression path format.
 * The conversion rules are as follows:
 * 1. If the pattern or path contains a period ('.') or a colon (':'), it will be escaped to a slash ('/').
 * 2. Supports wildcard characters:
 *    - `#` represents a single word.
 *    - `*` represents multiple words.
 *
 * After conversion, the method uses AntPathMatcher to perform the matching.
 *
 * @param pattern The pattern to match, supporting wildcard characters and escaping.
 * @param path    The path to match, supporting wildcard characters and escaping.
 * @return true if the path matches the pattern; false otherwise.
 */
@Override
public boolean matches(String pattern, String path) {
    String convertedPattern = this.convertToAntPattern(pattern);
    String convertedPath = this.convertToAntPath(path);

    return this.matcher.match(convertedPattern, convertedPath);
}
```

