package com.ffdev.diff.services;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.api.dtos.DiffResonseDTO;
import com.ffdev.diff.exceptions.DiffPartNotFoundException;
import com.ffdev.diff.repositories.DiffPartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayName("Diff CService")
class DiffServiceTest {

    @Mock
    private DiffPartRepository repository;

    @Mock
    private DiffCheckService checkService;

    private DiffService service;

    @BeforeEach
    public void setup() {
        openMocks(this);

        service = new DiffService(repository, checkService);
    }

    @Nested
    @DisplayName("when saving left")
    class SaveLeft {

        @Test
        @DisplayName("should persist received data")
        public void shouldPersist() {
            String testId = "any-id";
            String testData = "any-data";

            service.saveLeft(testId, testData);

            verify(repository).save(eq(DiffPart.LEFT), eq(testId), eq(testData));
        }
    }

    @Nested
    @DisplayName("when saving right")
    class SaveRight {

        @Test
        @DisplayName("should persist received data")
        public void shouldPersist() {
            String testId = "any-id";
            String testData = "any-data";

            service.saveRight(testId, testData);

            verify(repository).save(eq(DiffPart.RIGHT), eq(testId), eq(testData));
        }
    }

    @Nested
    @DisplayName("when getting diff by ID")
    class GetById {

        @Test
        @DisplayName("should throw an exception if left part is not found")
        public void shouldThrowNotFoundForLeftPart() {
            String testId = "any-id";

            withDiffPart(testId, null, "some-data");

            assertThrows(DiffPartNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        @DisplayName("should throw an exception if right part is not found")
        public void shouldThrowNotFoundForRightPart() {
            String testId = "any-id";

            withDiffPart(testId, "some-data", null);

            assertThrows(DiffPartNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        @DisplayName("should return diff when both parts are present")
        public void shouldReturnDiff() {
            String testId = "any-id";
            String testData = "any-data";

            DiffResonseDTO expectedResult = new DiffResonseDTO("EQUAL", emptyList());
            when(checkService.getDiff(eq(testData), eq(testData)))
                    .thenReturn(expectedResult);

            withDiffPart(testId, testData, testData);

            assertEquals(expectedResult, service.getById(testId));
        }

        private void withDiffPart(String id, String left, String right) {
            when(repository.getById(eq(DiffPart.LEFT), eq(id)))
                    .thenReturn(Optional.ofNullable(left));

            when(repository.getById(eq(DiffPart.RIGHT), eq(id)))
                    .thenReturn(Optional.ofNullable(right));
        }
    }
}