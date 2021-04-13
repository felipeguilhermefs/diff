package com.ffdev.diff.domain.repositories;

import com.ffdev.diff.domain.entities.DiffSide;
import com.ffdev.diff.shared.AbstractRedisIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.ffdev.diff.domain.enums.Side.LEFT;
import static com.ffdev.diff.domain.enums.Side.RIGHT;
import static com.ffdev.diff.shared.helpers.RandomHelper.uuid;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link DiffSideRepositoryIT} guarantees that data is stored and retrieved from repository as we want.
 *
 * <p>A real redis instance is used, and should be available to this integration test.
 *
 * <p>Using a solution like "testcontainers" would remove the need of a redis instance running locally.
 */
@DisplayName("Diff Side Repository")
class DiffSideRepositoryIT extends AbstractRedisIT {

    @Value("${diff.cache.ttl-minutes}")
    private long timeToLive;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DiffSideRepository repository;

    @Nested
    @DisplayName("when saving a diff side")
    class Save {

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should save diff side in a specific key")
        public void shouldSave(DiffSide testData) {

            repository.save(testData);

            var key = testKey(testData);
            var dataSaved = redisTemplate.opsForValue().get(key);

            assertEquals(testData.data(), dataSaved);
        }

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should set a expiration time diff side data")
        public void shouldExpire(DiffSide testData) {

            repository.save(testData);

            var key = testKey(testData);
            var remainingMinutes = redisTemplate.getExpire(key, TimeUnit.MINUTES);

            assertNotNull(remainingMinutes);
            assertTrue(remainingMinutes <= timeToLive && remainingMinutes > 0);
        }
    }

    @Nested
    @DisplayName("when searching data by side and ID")
    class FetchDataBySideAndDiffId {

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should find a specific key")
        public void shouldFind(DiffSide testData) {
            var key = testKey(testData);
            redisTemplate.opsForValue().set(key, testData.data());

            var dataRetrieved = repository.fetchDataBySideAndDiffId(testData.side(), testData.diffId());

            assertTrue(dataRetrieved.isPresent());
            assertEquals(testData.data(), dataRetrieved.get());
        }

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should return empty if a key does not exists")
        public void shouldNotFind(DiffSide testData) {
            var key = testKey(testData);
            redisTemplate.delete(key);

            var dataRetrieved = repository.fetchDataBySideAndDiffId(testData.side(), testData.diffId());

            assertFalse(dataRetrieved.isPresent());
        }
    }

    private String testKey(DiffSide testData) {
        return "diff:" + testData.diffId() + ":" + testData.side().getId();
    }

    /**
     * Provides test data for each test case, so we don't need to redefine it.
     */
    static class TestDataProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    new DiffSide(LEFT, uuid(), "{\"id\":123,\"message\":\"some json\"}"),
                    new DiffSide(RIGHT, uuid(), "some plain text"),
                    new DiffSide(RIGHT, uuid(), "<html><head></head><body><h1>some html</h1></body></html>"),
                    new DiffSide(LEFT, uuid(), "7B2EUCQfTVqbDyEYySKs444Gex/SxMewVBP5MPbS3ktgmxwwzGEaB")
            ).map(Arguments::of);
        }
    }
}