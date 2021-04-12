package com.ffdev.diff.repositories;

import com.ffdev.diff.domain.enums.DiffPart;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class DiffPartRepository {

    private final StringRedisTemplate redisTemplate;
    private final long ttl;

    public DiffPartRepository(
            StringRedisTemplate redisTemplate,
            @Value("${diff.parts.ttl-minutes}") long ttl
    ) {
        this.redisTemplate = redisTemplate;
        this.ttl = ttl;
    }

    /**
     * savePart will persist the requested diff part.
     * <p>
     * Given its current transient nature that data will be stored for a limited amount of time.
     *
     * @param data the actual data that will be compared
     * @param id   represents diff ID
     * @param part which part it represents
     */
    public void save(@NotNull DiffPart part, @NotNull String id, @NotNull String data) {
        String key = getKey(id, part);
        redisTemplate.opsForValue().set(key, data, ttl, TimeUnit.MINUTES);
    }

    public Optional<String> getById(@NotNull DiffPart part, @NotNull String id) {
        String key = getKey(id, part);
        String data = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(data);
    }

    // key format example: diff:some-id:left
    private String getKey(String id, DiffPart part) {
        return String.format("diff:%s:%s", id, part.getId());
    }
}
