package com.ffdev.diff.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DiffReadRepository {

    private final StringRedisTemplate redisTemplate;

    public DiffReadRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<String> getById(@NotNull String id) {
        String key = getKey(id);
        String data = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(data);
    }

    // key format example: diff:some-id
    private String getKey(String id) {
        return String.format("diff:%s", id);
    }
}
