package com.ffdev.diff.domain.repositories;

import com.ffdev.diff.domain.enums.DiffSide;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class DiffSideRepository {

    private final StringRedisTemplate redisTemplate;
    private final long timeToLive;

    public DiffSideRepository(
            StringRedisTemplate redisTemplate,
            @Value("${diff.cache.ttl-minutes}") long timeToLive
    ) {
        this.redisTemplate = redisTemplate;
        this.timeToLive = timeToLive;
    }

    /**
     * save will persist the requested diff side.
     * <p>
     * Given its current transient nature that data will be stored for a limited amount of time.
     *
     * @param data the actual data that will be compared
     * @param id   represents diff ID
     * @param side which side it represents
     */
    public void save(@NotNull DiffSide side, @NotNull String id, @NotNull String data) {
        String key = getKey(id, side);
        redisTemplate.opsForValue().set(key, data, timeToLive, TimeUnit.MINUTES);
    }

    public Optional<String> getById(@NotNull DiffSide side, @NotNull String id) {
        String key = getKey(id, side);
        String data = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(data);
    }

    // key format example: diff:some-id:left
    private String getKey(String id, DiffSide side) {
        return String.format("diff:%s:%s", id, side.getId());
    }
}
