package com.ffdev.diff.domain.repositories;

import com.ffdev.diff.domain.enums.DiffSide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Diff Side Repository")
class DiffSideRepositoryTest {

    @Value("${diff.cache.ttl-minutes}")
    private long timeToLive;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DiffSideRepository repository;

    @AfterEach
    public void cleanup() {
        Set<String> allKeys = redisTemplate.keys("*");
        if (allKeys != null) {
            redisTemplate.delete(allKeys);
        }
    }

    @Nested
    @DisplayName("when saving a diff side")
    class Save {

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should save diff side in a specific key")
        public void shouldSave(String testKey, DiffSide side, String id, String data) {

            repository.save(side, id, data);

            String dataSaved = redisTemplate.opsForValue().get(testKey);

            assertEquals(data, dataSaved);
        }

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should set a expiration time to left side data")
        public void shouldExpire(String testKey, DiffSide side, String id, String data) {

            repository.save(side, id, data);

            Long remainingMinutes = redisTemplate.getExpire(testKey, TimeUnit.MINUTES);

            assertNotNull(remainingMinutes);
            assertTrue(remainingMinutes <= timeToLive);
        }
    }

    @Nested
    @DisplayName("when searching side by ID")
    class GetSideById {

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should find a specific key")
        public void shouldFind(String testKey, DiffSide side, String id, String data) {
            redisTemplate.opsForValue().set(testKey, data);

            Optional<String> dataRetrieved = repository.getById(side, id);

            assertTrue(dataRetrieved.isPresent());
            assertEquals(data, dataRetrieved.get());
        }

        @ParameterizedTest
        @ArgumentsSource(TestDataProvider.class)
        @DisplayName("should not find a key if it does not exists")
        public void shouldNotFind(String testKey, DiffSide side, String id, String data) {
            redisTemplate.delete(testKey);

            Optional<String> dataRetrieved = repository.getById(side, id);

            assertFalse(dataRetrieved.isPresent());
        }
    }

    static class TestDataProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    arguments(
                            "diff:07649074-2494-4643-b557-b48c038b789c:left",
                            DiffSide.LEFT,
                            "07649074-2494-4643-b557-b48c038b789c",
                            "{\"id\":123,\"message\":\"some json\"}"
                    ),
                    arguments(
                            "diff:c07e5519-2c35-484e-aed9-d56770acf9c7:right",
                            DiffSide.RIGHT,
                            "c07e5519-2c35-484e-aed9-d56770acf9c7",
                            "some plain text"
                    ),
                    arguments(
                            "diff:403c1d9c-5e2e-401d-935b-e1f3a25531a8:right",
                            DiffSide.RIGHT,
                            "403c1d9c-5e2e-401d-935b-e1f3a25531a8",
                            "<html><head></head><body><h1>some html</h1></body></html>"
                    ),
                    arguments(
                            "diff:1fc71390-4027-4d9d-8136-58a9c495c98e:left",
                            DiffSide.LEFT,
                            "1fc71390-4027-4d9d-8136-58a9c495c98e",
                            "7B2EUCQfTVqbDyEYySKs444Gex/SxMewVBP5MPbS3ktgmxwwzGEaB"
                    )
            );
        }
    }
}