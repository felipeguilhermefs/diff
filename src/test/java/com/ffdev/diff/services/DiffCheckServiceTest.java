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

        assertEquals("EQUAL", result.getStatus());
        assertTrue(result.getChanges().isEmpty());
    }

    @Test
    @DisplayName("should return different sizes result")
    public void shouldReturnDifferentSizes() {
        DiffResultDTO result = service.getDiff("some-data", "other-data");

        // some-data (9 chars) != other-data (10 chars)
        assertEquals("DIFFERENT_SIZES", result.getStatus());

        // no changes are identified at the moment
        assertTrue(result.getChanges().isEmpty());
    }

    @Test
    @DisplayName("should return changes at the start")
    public void shouldReturnChangesAtStart() {
        DiffResultDTO result = service.getDiff("some-data", "cene-data");

        assertEquals("DIFFERENT", result.getStatus());

        // some-data
        // |||
        // cene-data
        assertEquals(1, result.getChanges().size());
        assertEquals(0L, result.getChanges().get(0).getOffset());
        assertEquals(3L, result.getChanges().get(0).getLength());
    }

    @Test
    @DisplayName("should return changes at the end")
    public void shouldReturnChangesAtEnd() {
        DiffResultDTO result = service.getDiff("some-data", "some-yolo");

        assertEquals("DIFFERENT", result.getStatus());
        // some-data
        //      ||||
        // some-yolo
        assertEquals(1, result.getChanges().size());
        assertEquals(5L, result.getChanges().get(0).getOffset());
        assertEquals(4L, result.getChanges().get(0).getLength());
    }

    @Test
    @DisplayName("should return changes in the middle")
    public void shouldReturnChangesInMiddle() {
        DiffResultDTO result = service.getDiff("some-data", "solo:beta");

        assertEquals("DIFFERENT", result.getStatus());
        // some-data
        //   |||||
        // solo:beta
        assertEquals(1, result.getChanges().size());
        assertEquals(2L, result.getChanges().get(0).getOffset());
        assertEquals(5L, result.getChanges().get(0).getLength());
    }

    @Test
    @DisplayName("should return multiple changes")
    public void shouldReturnMultipleChanges() {
        DiffResultDTO result = service.getDiff("some-data", "iota-bela");

        assertEquals("DIFFERENT", result.getStatus());
        // some-data
        // | || |||
        // iota-bela
        assertEquals(3, result.getChanges().size());
        assertEquals(0L, result.getChanges().get(0).getOffset());
        assertEquals(1L, result.getChanges().get(0).getLength());
        assertEquals(2L, result.getChanges().get(1).getOffset());
        assertEquals(2L, result.getChanges().get(1).getLength());
        assertEquals(5L, result.getChanges().get(2).getOffset());
        assertEquals(3L, result.getChanges().get(2).getLength());
    }
}