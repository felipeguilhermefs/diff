package com.ffdev.diff.shared.helpers;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.redis.client-type=jedis",
                "spring.redis.host=localhost",
                "spring.redis.port=6379",
                "spring.redis.database=1",
        })
@ActiveProfiles("test")
public abstract class AbstractRedisIT {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @AfterEach
    public void cleanup() {
        var allKeys = redisTemplate.keys("*");
        if (allKeys != null) {
            redisTemplate.delete(allKeys);
        }
    }
}
