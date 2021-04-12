package com.ffdev.diff.services;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.dtos.DiffResultDTO;
import com.ffdev.diff.exceptions.DiffPartNotFoundException;
import com.ffdev.diff.repositories.DiffPartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayName("Diff Query Service")
class DiffQueryServiceTest {

    @Mock
    private DiffPartRepository repository;

    private DiffQueryService service;

    @BeforeEach
    public void setup() {
        openMocks(this);

        service = new DiffQueryService(repository);
    }

    @Test
    @DisplayName("should throw an exception if left part is not found")
    public void shouldThrowNotFoundForLeftPart() {
        String testId = "some-id";

        withDiffPart(testId, null, "some-data");

        assertThrows(DiffPartNotFoundException.class, () -> service.getById(testId));
    }

    @Test
    @DisplayName("should throw an exception if right part is not found")
    public void shouldThrowNotFoundForRightPart() {
        String testId = "some-id";

        withDiffPart(testId, "some-data", null);

        assertThrows(DiffPartNotFoundException.class, () -> service.getById(testId));
    }

    @Test
    @DisplayName("should return equal result")
    public void shouldReturnEqual() {
        String testId = "some-id";

        when(repository.getById(any(), eq(testId)))
                .thenReturn(Optional.of("some-data"));

        DiffResultDTO result = service.getById(testId);

        assertEquals("EQUAL", result.getStatus());
        assertTrue(result.getChanges().isEmpty());
    }

    @Test
    @DisplayName("should return different sizes result")
    public void shouldReturnDifferentSizes() {
        String testId = "some-id";

        withDiffPart(testId, "some-data", "other-data");

        DiffResultDTO result = service.getById(testId);

        assertEquals("DIFFERENT_SIZES", result.getStatus());
        assertTrue(result.getChanges().isEmpty());
    }

    @Test
    @DisplayName("should return changes at the start")
    public void shouldReturnChangesAtStart() {
        String testId = "some-id";

        withDiffPart(testId, "some-data", "cene-data");

        DiffResultDTO result = service.getById(testId);

        assertEquals("DIFFERENT", result.getStatus());
        assertEquals(1, result.getChanges().size());
        assertEquals(0L, result.getChanges().get(0).getOffset());
        assertEquals(3L, result.getChanges().get(0).getLength());
    }

    @Test
    @DisplayName("should return changes at the end")
    public void shouldReturnChangesAtEnd() {
        String testId = "some-id";

        withDiffPart(testId, "some-data", "some-yolo");

        DiffResultDTO result = service.getById(testId);

        assertEquals("DIFFERENT", result.getStatus());
        assertEquals(1, result.getChanges().size());
        assertEquals(5L, result.getChanges().get(0).getOffset());
        assertEquals(4L, result.getChanges().get(0).getLength());
    }

    @Test
    @DisplayName("should return changes in the middle")
    public void shouldReturnChangesInMiddle() {
        String testId = "some-id";

        withDiffPart(testId, "some-data", "solo:beta");

        DiffResultDTO result = service.getById(testId);

        assertEquals("DIFFERENT", result.getStatus());
        assertEquals(1, result.getChanges().size());
        assertEquals(2L, result.getChanges().get(0).getOffset());
        assertEquals(5L, result.getChanges().get(0).getLength());
    }

    @Test
    @DisplayName("should return multiple changes")
    public void shouldReturnMultipleChanges() {
        String testId = "some-id";

        withDiffPart(testId, "some-data", "iota-bela");

        DiffResultDTO result = service.getById(testId);

        assertEquals("DIFFERENT", result.getStatus());
        assertEquals(3, result.getChanges().size());
        assertEquals(0L, result.getChanges().get(0).getOffset());
        assertEquals(1L, result.getChanges().get(0).getLength());
        assertEquals(2L, result.getChanges().get(1).getOffset());
        assertEquals(2L, result.getChanges().get(1).getLength());
        assertEquals(5L, result.getChanges().get(2).getOffset());
        assertEquals(3L, result.getChanges().get(2).getLength());
    }

    private void withDiffPart(String id, String left, String right) {
        when(repository.getById(eq(DiffPart.LEFT), eq(id)))
                .thenReturn(Optional.ofNullable(left));

        when(repository.getById(eq(DiffPart.RIGHT), eq(id)))
                .thenReturn(Optional.ofNullable(right));
    }
}