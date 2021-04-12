package com.ffdev.diff.repositories;

import com.ffdev.diff.helpers.PostDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Diff Read Repository")
class DiffReadRepositoryTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DiffReadRepository repository;

    @AfterEach
    public void cleanup() {
        Set<String> allKeys = redisTemplate.keys("*");
        if (allKeys != null) {
            redisTemplate.delete(allKeys);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PostDataProvider.class)
    @DisplayName("should find a specific key")
    public void shouldFind(String id, String data) {
        redisTemplate.opsForValue().set("diff:" + id, data);

        Optional<String> dataRetrieved = repository.getById(id);

        assertTrue(dataRetrieved.isPresent());
        assertEquals(data, dataRetrieved.get());
    }

    @ParameterizedTest
    @ArgumentsSource(PostDataProvider.class)
    @DisplayName("should not find a key if it does not exists")
    public void shouldNotFind(String id, String data) {
        redisTemplate.delete("diff:" + id);

        Optional<String> dataRetrieved = repository.getById(id);

        assertFalse(dataRetrieved.isPresent());
    }
}