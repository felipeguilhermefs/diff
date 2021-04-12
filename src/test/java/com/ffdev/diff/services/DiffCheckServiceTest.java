package com.ffdev.diff.services;

import com.ffdev.diff.dtos.DiffResultDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Diff Check Service")
class DiffCheckServiceTest {

    private final DiffCheckService service = new DiffCheckService();

    @Test
    @DisplayName("should return equal result")
    public void shouldReturnEqual() {
        DiffResultDTO result = service.getDiff("some-data", "some-data");

        assertEquals("EQUAL", result.status());
        assertTrue(result.changes().isEmpty());
    }

    @Test
    @DisplayName("should return different sizes result")
    public void shouldReturnDifferentSizes() {
        DiffResultDTO result = service.getDiff("some-data", "other-data");

        // some-data (9 chars) != other-data (10 chars)
        assertEquals("DIFFERENT_SIZES", result.status());

        // no changes are identified at the moment
        assertTrue(result.changes().isEmpty());
    }

    @Test
    @DisplayName("should return changes at the start")
    public void shouldReturnChangesAtStart() {
        DiffResultDTO result = service.getDiff("some-data", "cene-data");

        assertEquals("DIFFERENT", result.status());

        // some-data
        // |||
        // cene-data
        assertEquals(1, result.changes().size());
        assertEquals(0L, result.changes().get(0).offset());
        assertEquals(3L, result.changes().get(0).length());
    }

    @Test
    @DisplayName("should return changes at the end")
    public void shouldReturnChangesAtEnd() {
        DiffResultDTO result = service.getDiff("some-data", "some-yolo");

        assertEquals("DIFFERENT", result.status());
        // some-data
        //      ||||
        // some-yolo
        assertEquals(1, result.changes().size());
        assertEquals(5L, result.changes().get(0).offset());
        assertEquals(4L, result.changes().get(0).length());
    }

    @Test
    @DisplayName("should return changes in the middle")
    public void shouldReturnChangesInMiddle() {
        DiffResultDTO result = service.getDiff("some-data", "solo:beta");

        assertEquals("DIFFERENT", result.status());
        // some-data
        //   |||||
        // solo:beta
        assertEquals(1, result.changes().size());
        assertEquals(2L, result.changes().get(0).offset());
        assertEquals(5L, result.changes().get(0).length());
    }

    @Test
    @DisplayName("should return multiple changes")
    public void shouldReturnMultipleChanges() {
        DiffResultDTO result = service.getDiff("some-data", "iota-bela");

        assertEquals("DIFFERENT", result.status());
        // some-data
        // | || |||
        // iota-bela
        assertEquals(3, result.changes().size());
        assertEquals(0L, result.changes().get(0).offset());
        assertEquals(1L, result.changes().get(0).length());
        assertEquals(2L, result.changes().get(1).offset());
        assertEquals(2L, result.changes().get(1).length());
        assertEquals(5L, result.changes().get(2).offset());
        assertEquals(3L, result.changes().get(2).length());
    }
}