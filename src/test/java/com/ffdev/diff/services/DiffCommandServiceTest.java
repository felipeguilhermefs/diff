package com.ffdev.diff.services;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.helpers.PostDataProvider;
import com.ffdev.diff.repositories.DiffWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayName("Diff Command Service")
class DiffCommandServiceTest {

    @Mock
    private DiffWriteRepository repository;

    private DiffCommandService service;

    @BeforeEach
    public void setup() {
        openMocks(this);

        service = new DiffCommandService(repository);
    }

    @Nested
    @DisplayName("when saving left")
    class SaveLeft {

        @ParameterizedTest
        @ArgumentsSource(PostDataProvider.class)
        @DisplayName("should persist received data")
        public void shouldPersist(String id, String data) {
            service.saveLeft(id, data);

            verify(repository).savePart(eq(id), eq(data), eq(DiffPart.LEFT));
        }
    }

    @Nested
    @DisplayName("when saving right")
    class SaveRight {

        @ParameterizedTest
        @ArgumentsSource(PostDataProvider.class)
        @DisplayName("should persist received data")
        public void shouldPersist(String id, String data) {
            service.saveRight(id, data);

            verify(repository).savePart(eq(id), eq(data), eq(DiffPart.RIGHT));
        }
    }
}