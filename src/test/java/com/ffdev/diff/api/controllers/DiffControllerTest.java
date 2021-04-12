package com.ffdev.diff.api.controllers;

import com.ffdev.diff.api.dtos.ResponseDTO;
import com.ffdev.diff.helpers.PostDataProvider;
import com.ffdev.diff.helpers.RandomIdProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Diff API")
class DiffControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @AfterEach
    public void cleanup() {
        Set<String> allKeys = redisTemplate.keys("*");
        if (allKeys != null) {
            redisTemplate.delete(allKeys);
        }
    }

    @Nested
    @DisplayName("when saving left")
    class SaveLeft {

        @ParameterizedTest
        @ArgumentsSource(PostDataProvider.class)
        @DisplayName("should accept post data for given ID")
        public void shouldAccept(String id, String data) {
            HttpEntity<String> body = new HttpEntity<>(data);

            ResponseEntity<Void> response = restTemplate.postForEntity(
                    "http://localhost:" + port + "/v1/diff/{id}/left",
                    body,
                    Void.class,
                    id
            );

            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

            String dataSaved = redisTemplate.opsForValue().get("diff:" + id + ":left");

            assertEquals(data, dataSaved);
        }
    }

    @Nested
    @DisplayName("when saving right")
    class SaveRight {

        @ParameterizedTest
        @ArgumentsSource(PostDataProvider.class)
        @DisplayName("should accept post data for given ID")
        public void shouldAccept(String id, String data) {
            HttpEntity<String> body = new HttpEntity<>(data);

            ResponseEntity<Void> response = restTemplate.postForEntity(
                    "http://localhost:" + port + "/v1/diff/{id}/right",
                    body,
                    Void.class,
                    id
            );

            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

            String dataSaved = redisTemplate.opsForValue().get("diff:" + id + ":right");

            assertEquals(data, dataSaved);
        }
    }

    @Nested
    @DisplayName("when retrieving diff")
    class GetDiff {

        @ParameterizedTest
        @ArgumentsSource(RandomIdProvider.class)
        @DisplayName("should return diff data for given ID when both are equal")
        public void shouldReturnOkWhenEqual(String id) {
            HttpEntity<String> left = new HttpEntity<>("some-data");

            ResponseEntity<Void> leftResponse = restTemplate.postForEntity(
                    "http://localhost:" + port + "/v1/diff/{id}/left",
                    left,
                    Void.class,
                    id
            );

            assertEquals(HttpStatus.ACCEPTED, leftResponse.getStatusCode());

            HttpEntity<String> right = new HttpEntity<>("some-data");

            ResponseEntity<Void> rightResponse = restTemplate.postForEntity(
                    "http://localhost:" + port + "/v1/diff/{id}/right",
                    right,
                    Void.class,
                    id
            );

            assertEquals(HttpStatus.ACCEPTED, rightResponse.getStatusCode());

            ResponseEntity<ResponseDTO> response = restTemplate.getForEntity(
                    "http://localhost:" + port + "/v1/diff/{id}",
                    ResponseDTO.class,
                    id
            );

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(new ResponseDTO("EQUAL", emptyList()), response.getBody());
        }
    }
}