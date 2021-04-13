package com.ffdev.diff.domain.repositories;

import com.ffdev.diff.domain.enums.Side;
import com.ffdev.diff.domain.models.DiffSide;
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
     */
    public void save(@NotNull DiffSide diffSide) {
        String key = getKey(diffSide.id(), diffSide.side());
        redisTemplate.opsForValue().set(key, diffSide.data(), timeToLive, TimeUnit.MINUTES);
    }

    public Optional<String> getById(@NotNull Side side, @NotNull String id) {
        String key = getKey(id, side);
        String data = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(data);
    }

    // key format example: diff:some-id:left
    private String getKey(String id, Side side) {
        return String.format("diff:%s:%s", id, side.getId());
    }
}
