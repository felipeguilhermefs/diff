package com.ffdev.diff.domain.services;

import com.ffdev.diff.domain.models.Diff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ffdev.diff.domain.enums.DiffResult.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Diff Check Service")
class DiffCheckServiceTest {

    private final DiffCheckService service = new DiffCheckService();

    @Test
    @DisplayName("should return equal result")
    public void shouldReturnEqual() {
        Diff result = service.getDiff("some-data", "some-data");

        assertEquals(EQUAL, result.result());
        assertTrue(result.differences().isEmpty());
    }

    @Test
    @DisplayName("should return different sizes result")
    public void shouldReturnDifferentSizes() {
        Diff result = service.getDiff("some-data", "other-data");

        // some-data (9 chars) != other-data (10 chars)
        assertEquals(DIFFERENT_SIZES, result.result());

        // no differences are identified at the moment
        assertTrue(result.differences().isEmpty());
    }

    @Test
    @DisplayName("should return differences at the start")
    public void shouldReturnChangesAtStart() {
        Diff result = service.getDiff("some-data", "cene-data");

        assertEquals(DIFFERENT, result.result());

        // some-data
        // |||
        // cene-data
        assertEquals(1, result.differences().size());
        assertEquals(0L, result.differences().get(0).offset());
        assertEquals(3L, result.differences().get(0).length());
    }

    @Test
    @DisplayName("should return differences at the end")
    public void shouldReturnChangesAtEnd() {
        Diff result = service.getDiff("some-data", "some-yolo");

        assertEquals(DIFFERENT, result.result());
        // some-data
        //      ||||
        // some-yolo
        assertEquals(1, result.differences().size());
        assertEquals(5L, result.differences().get(0).offset());
        assertEquals(4L, result.differences().get(0).length());
    }

    @Test
    @DisplayName("should return differences in the middle")
    public void shouldReturnChangesInMiddle() {
        Diff result = service.getDiff("some-data", "solo:beta");

        assertEquals(DIFFERENT, result.result());
        // some-data
        //   |||||
        // solo:beta
        assertEquals(1, result.differences().size());
        assertEquals(2L, result.differences().get(0).offset());
        assertEquals(5L, result.differences().get(0).length());
    }

    @Test
    @DisplayName("should return multiple differences")
    public void shouldReturnMultipleChanges() {
        Diff result = service.getDiff("some-data", "iota-bela");

        assertEquals(DIFFERENT, result.result());
        // some-data
        // | || |||
        // iota-bela
        assertEquals(3, result.differences().size());
        assertEquals(0L, result.differences().get(0).offset());
        assertEquals(1L, result.differences().get(0).length());
        assertEquals(2L, result.differences().get(1).offset());
        assertEquals(2L, result.differences().get(1).length());
        assertEquals(5L, result.differences().get(2).offset());
        assertEquals(3L, result.differences().get(2).length());
    }
}