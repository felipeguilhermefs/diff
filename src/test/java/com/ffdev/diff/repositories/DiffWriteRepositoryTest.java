package com.ffdev.diff.repositories;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.helpers.PostDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Diff Write Repository")
class DiffWriteRepositoryTest {

    @Value("${diff.parts.ttl-minutes}")
    private long ttl;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DiffWriteRepository repository;

    @AfterEach
    public void cleanup() {
        Set<String> allKeys = redisTemplate.keys("*");
        if (allKeys != null) {
            redisTemplate.delete(allKeys);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PostDataProvider.class)
    @DisplayName("should save the left part diff in a specific key")
    public void shouldSaveLeft(String id, String data) {

        repository.savePart(id, data, DiffPart.LEFT);

        String dataSaved = redisTemplate.opsForValue().get("diff:" + id + ":left");

        assertEquals(data, dataSaved);
    }

    @ParameterizedTest
    @ArgumentsSource(PostDataProvider.class)
    @DisplayName("should save the right part diff in a specific key")
    public void shouldSaveRight(String id, String data) {

        repository.savePart(id, data, DiffPart.RIGHT);

        String dataSaved = redisTemplate.opsForValue().get("diff:" + id + ":right");

        assertEquals(data, dataSaved);
    }

    @ParameterizedTest
    @ArgumentsSource(PostDataProvider.class)
    @DisplayName("should set a expiration time to left part data")
    public void shouldExpireLeft(String id, String data) {

        repository.savePart(id, data, DiffPart.LEFT);

        Long remainingMinutes = redisTemplate.getExpire("diff:" + id + ":left", TimeUnit.MINUTES);

        assertNotNull(remainingMinutes);
        assertTrue(remainingMinutes <= ttl);
    }

    @ParameterizedTest
    @ArgumentsSource(PostDataProvider.class)
    @DisplayName("should set a expiration time to right part data")
    public void shouldExpireRight(String id, String data) {

        repository.savePart(id, data, DiffPart.RIGHT);

        Long remainingMinutes = redisTemplate.getExpire("diff:" + id + ":right", TimeUnit.MINUTES);

        assertNotNull(remainingMinutes);
        assertTrue(remainingMinutes <= ttl);
    }
}