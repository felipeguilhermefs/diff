package com.ffdev.diff.services;

import com.ffdev.diff.dtos.DiffResultDTO;
import com.ffdev.diff.exceptions.DiffNotFoundException;
import com.ffdev.diff.repositories.DiffReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayName("Diff Query Service")
class DiffQueryServiceTest {

    @Mock
    private DiffReadRepository repository;

    private DiffQueryService service;

    @BeforeEach
    public void setup() {
        openMocks(this);

        service = new DiffQueryService(repository);
    }

    @Test
    @DisplayName("should return data found")
    public void shouldReturnDataFound() {
        String testId = "some-id";

        when(repository.getById(eq(testId)))
                .thenReturn(Optional.of("some-data"));

        DiffResultDTO result = service.getById(testId);

        assertEquals("some-result-status", result.getStatus());
        assertEquals(1, result.getChanges().size());
        assertEquals("some-action", result.getChanges().get(0).getAction());
        assertEquals(0L, result.getChanges().get(0).getOffset());
        assertEquals(9L, result.getChanges().get(0).getLength());
    }

    @Test
    @DisplayName("should throw an exception if diff is not found")
    public void shouldReturnEmptyString() {
        String testId = "some-id";

        when(repository.getById(eq(testId)))
                .thenReturn(Optional.empty());

        assertThrows(DiffNotFoundException.class, () -> service.getById(testId));
    }
}