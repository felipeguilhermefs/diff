package com.ffdev.diff.api.controllers;

import com.ffdev.diff.api.dtos.ErrorDTO;
import com.ffdev.diff.api.dtos.ResponseDTO;
import com.ffdev.diff.domain.enums.DiffSide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.UUID;

import static com.ffdev.diff.api.enums.ErrorCode.*;
import static com.ffdev.diff.domain.enums.DiffSide.LEFT;
import static com.ffdev.diff.domain.enums.DiffSide.RIGHT;
import static com.ffdev.diff.helpers.Base64Helper.encodeB64;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Diff API")
class DiffControllerIT {

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

    @Test
    @DisplayName("should return 409 if left side is not Base64")
    public void shouldReturn409IfLeftNotBase64() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        ResponseEntity<ErrorDTO> response = postError(LEFT, testId, testData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BASE64_INVALID, error.code());
        assertEquals("Invalid base 64 data", error.message());
    }

    @Test
    @DisplayName("should return 409 if right side is not Base64")
    public void shouldReturn409IfRightNotBase64() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        ResponseEntity<ErrorDTO> response = postError(RIGHT, testId, testData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BASE64_INVALID, error.code());
        assertEquals("Invalid base 64 data", error.message());
    }

    @Test
    @DisplayName("should return 409 if left side is not JSON")
    public void shouldReturn409IfLeftNotJSON() {
        String testId = generateRandom();
        String testData = "some-data";

        ResponseEntity<ErrorDTO> response = postError(LEFT, testId, encodeB64(testData));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(JSON_INVALID, error.code());
        assertEquals("Invalid JSON data", error.message());
    }

    @Test
    @DisplayName("should return 409 if right side is not JSON")
    public void shouldReturn409IfRightNotJSON() {
        String testId = generateRandom();
        String testData = "some-data";

        ResponseEntity<ErrorDTO> response = postError(RIGHT, testId, encodeB64(testData));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(JSON_INVALID, error.code());
        assertEquals("Invalid JSON data", error.message());
    }

    @Test
    @DisplayName("should return 404 if right side is missing")
    public void shouldReturn404IfNoRightSide() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, testData));

        ResponseEntity<ErrorDTO> response = getErrorDiff(testId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(RIGHT_NOT_FOUND, error.code());
        assertEquals("Diff right side was not found", error.message());
    }

    @Test
    @DisplayName("should return 404 if left side is missing")
    public void shouldReturn404IfNoLeftSide() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(RIGHT, testId, testData));

        ResponseEntity<ErrorDTO> response = getErrorDiff(testId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(LEFT_NOT_FOUND, error.code());
        assertEquals("Diff left side was not found", error.message());
    }

    @Test
    @DisplayName("should return 200 with EQUAL result when both sides are the same")
    public void shouldReturn200WhenEqual() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, testData));
        assertEquals(HttpStatus.ACCEPTED, postEncoded(RIGHT, testId, testData));

        ResponseEntity<ResponseDTO> response = getDiff(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseDTO diff = response.getBody();
        assertNotNull(diff);
        assertEquals("EQUAL", diff.result());
        assertTrue(diff.differences().isEmpty());
    }

    @Test
    @DisplayName("should return 200 with DIFFERENT_SIZES result when sides are not equivalent in length")
    public void shouldReturn200WhenDifferentSizes() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, testData + testId.charAt(0)));
        assertEquals(HttpStatus.ACCEPTED, postEncoded(RIGHT, testId, testData));

        ResponseEntity<ResponseDTO> response = getDiff(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseDTO diff = response.getBody();
        assertNotNull(diff);
        assertEquals("DIFFERENT_SIZES", diff.result());
        assertTrue(diff.differences().isEmpty());
    }

    @Test
    @DisplayName("should return 200 with DIFFERENT result when sides are not equal")
    public void shouldReturn200WhenDifferent() {
        String testId = generateRandom();
        String lData = "{\"id\":123,\"message\":\"some json\"}";
        //                      ||     |               ||||
        String rData = "{\"id\":213,\"massage\":\"some JSON\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, lData));
        assertEquals(HttpStatus.ACCEPTED, postEncoded(RIGHT, testId, rData));

        ResponseEntity<ResponseDTO> response = getDiff(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseDTO diff = response.getBody();
        assertNotNull(diff);
        assertEquals("DIFFERENT", diff.result());
        assertEquals(3, diff.differences().size());
        assertEquals(6L, diff.differences().get(0).offset());
        assertEquals(2L, diff.differences().get(0).length());
        assertEquals(12L, diff.differences().get(1).offset());
        assertEquals(1L, diff.differences().get(1).length());
        assertEquals(26L, diff.differences().get(2).offset());
        assertEquals(4L, diff.differences().get(2).length());
    }

    @Test
    @DisplayName("cache should not change diff return 200")
    public void shouldReturn200WithCache() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, testData));
        assertEquals(HttpStatus.ACCEPTED, postEncoded(RIGHT, testId, testData));

        ResponseEntity<ResponseDTO> response = getDiff(testId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<ResponseDTO> cacheResponse = getDiff(testId);
        assertEquals(HttpStatus.OK, cacheResponse.getStatusCode());

        assertEquals(response.getBody(), cacheResponse.getBody());
    }

    @Test
    @DisplayName("diff should change if one of its sides changed")
    public void shouldRecalculateDiff() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, testData));
        assertEquals(HttpStatus.ACCEPTED, postEncoded(RIGHT, testId, testData));

        ResponseEntity<ResponseDTO> response = getDiff(testId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseDTO diff = response.getBody();
        assertNotNull(diff);
        assertEquals("EQUAL", diff.result());

        String otherData = "{\"id\":123,\"message\":\"other json\"}";
        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, otherData));

        ResponseEntity<ResponseDTO> newResponse = getDiff(testId);
        assertEquals(HttpStatus.OK, newResponse.getStatusCode());

        ResponseDTO newDiff = newResponse.getBody();
        assertNotNull(newDiff);
        assertEquals("DIFFERENT_SIZES", newDiff.result());
    }

    @Test
    @DisplayName("last diff should still be available if new diff side post errors out")
    public void shouldNotRecalculateDiff() {
        String testId = generateRandom();
        String testData = "{\"id\":123,\"message\":\"some json\"}";

        assertEquals(HttpStatus.ACCEPTED, postEncoded(LEFT, testId, testData));
        assertEquals(HttpStatus.ACCEPTED, postEncoded(RIGHT, testId, testData));

        ResponseEntity<ResponseDTO> response = getDiff(testId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(HttpStatus.BAD_REQUEST, postError(LEFT, testId, "any-bad-data").getStatusCode());

        ResponseEntity<ResponseDTO> cacheResponse = getDiff(testId);
        assertEquals(HttpStatus.OK, cacheResponse.getStatusCode());

        assertEquals(response.getBody(), cacheResponse.getBody());
    }

    private ResponseEntity<ResponseDTO> getDiff(String id) {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/v1/diff/{id}",
                ResponseDTO.class,
                id
        );
    }

    private HttpStatus postEncoded(DiffSide side, String id, String data) {
        HttpEntity<String> body = new HttpEntity<>(encodeB64(data));

        return restTemplate.postForEntity(
                "http://localhost:" + port + "/v1/diff/{id}/" + side.getId(),
                body,
                Void.class,
                id
        ).getStatusCode();
    }

    private ResponseEntity<ErrorDTO> postError(DiffSide side, String id, String data) {
        HttpEntity<String> body = new HttpEntity<>(data);

        return restTemplate.postForEntity(
                "http://localhost:" + port + "/v1/diff/{id}/" + side.getId(),
                body,
                ErrorDTO.class,
                id
        );
    }

    private ResponseEntity<ErrorDTO> getErrorDiff(String id) {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/v1/diff/{id}",
                ErrorDTO.class,
                id
        );
    }

    private String generateRandom() {
        return UUID.randomUUID().toString();
    }
}