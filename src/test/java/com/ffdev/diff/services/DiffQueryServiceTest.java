package com.ffdev.diff.services;

import com.ffdev.diff.repositories.DiffReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @ParameterizedTest
    @ValueSource(strings = "some-id")
    @DisplayName("should return data found")
    public void shouldReturnDataFound(String id) {

        when(repository.getById(eq(id)))
                .thenReturn(Optional.of("some-data"));

        assertEquals("some-data", service.getById(id));
    }

    @ParameterizedTest
    @ValueSource(strings = "some-id")
    @DisplayName("should return empty string when not found")
    public void shouldReturnEmptyString(String id) {

        when(repository.getById(eq(id)))
                .thenReturn(Optional.empty());

        assertEquals("", service.getById(id));
    }
}