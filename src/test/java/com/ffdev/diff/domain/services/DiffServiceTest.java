package com.ffdev.diff.domain.services;

import com.ffdev.diff.domain.enums.DiffSide;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
import com.ffdev.diff.domain.exceptions.InvalidBase64Exception;
import com.ffdev.diff.domain.exceptions.InvalidJsonException;
import com.ffdev.diff.domain.models.Diff;
import com.ffdev.diff.domain.models.Difference;
import com.ffdev.diff.domain.repositories.DiffSideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static com.ffdev.diff.domain.enums.DiffResult.EQUAL;
import static com.ffdev.diff.helpers.Base64Helper.encodeB64;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayName("Diff Service")
class DiffServiceTest {

    @Mock
    private DiffSideRepository sideRepository;

    @Mock
    private DiffCheckService checkService;

    private DiffService service;

    @BeforeEach
    public void setup() {
        openMocks(this);

        service = new DiffService(sideRepository, checkService);
    }

    @Nested
    @DisplayName("when saving left")
    class SaveLeft {

        @Test
        @DisplayName("should persist received data")
        public void shouldPersist() {
            String testId = "any-id";
            String testData = "{\"id\":123,\"message\":\"any json\"}";

            service.saveLeft(testId, encodeB64(testData));

            verify(sideRepository).save(eq(DiffSide.LEFT), eq(testId), eq(testData));
        }

        @Test
        @DisplayName("should thrown error if data is not Base64")
        public void shouldThrowB64Exception() {
            String testId = "any-id";
            String testData = "{\"id\":123,\"message\":\"any json\"}";

            assertThrows(InvalidBase64Exception.class, () -> service.saveLeft(testId, testData));

            verifyNoInteractions(sideRepository);
        }

        @Test
        @DisplayName("should thrown error if data is not JSON")
        public void shouldThrowJSONException() {
            String testId = "any-id";
            String testData = "any-data";

            assertThrows(InvalidJsonException.class, () -> service.saveLeft(testId, encodeB64(testData)));

            verifyNoInteractions(sideRepository);
        }
    }

    @Nested
    @DisplayName("when saving right")
    class SaveRight {

        @Test
        @DisplayName("should persist received data")
        public void shouldPersist() {
            String testId = "any-id";
            String testData = "{\"id\":123,\"message\":\"any json\"}";

            service.saveRight(testId, encodeB64(testData));

            verify(sideRepository).save(eq(DiffSide.RIGHT), eq(testId), eq(testData));
        }

        @Test
        @DisplayName("should thrown error if data is not Base64")
        public void shouldThrowB64Exception() {
            String testId = "any-id";
            String testData = "{\"id\":123,\"message\":\"any json\"}";

            assertThrows(InvalidBase64Exception.class, () -> service.saveRight(testId, testData));

            verifyNoInteractions(sideRepository);
        }

        @Test
        @DisplayName("should thrown error if data is not JSON")
        public void shouldThrowJSONException() {
            String testId = "any-id";
            String testData = "any-data";

            assertThrows(InvalidJsonException.class, () -> service.saveRight(testId, encodeB64(testData)));

            verifyNoInteractions(sideRepository);
        }
    }

    @Nested
    @DisplayName("when getting diff by ID")
    class GetById {

        @Test
        @DisplayName("should throw an exception if left side is not found")
        public void shouldThrowNotFoundForLeftSides() {
            String testId = "any-id";

            withDiffSides(testId, null, "some-data");

            assertThrows(DiffSideNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        @DisplayName("should throw an exception if right side is not found")
        public void shouldThrowNotFoundForRightSides() {
            String testId = "any-id";

            withDiffSides(testId, "some-data", null);

            assertThrows(DiffSideNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        @DisplayName("should return diff when both sides are present")
        public void shouldReturnDiff() {
            String testId = "any-id";
            String testData = "any-data";

            Diff expectedDiff = new Diff(EQUAL, singletonList(new Difference(30L, 6L)));
            when(checkService.getDiff(eq(testData), eq(testData)))
                    .thenReturn(expectedDiff);

            withDiffSides(testId, testData, testData);

            assertEquals(expectedDiff, service.getById(testId));
        }

        private void withDiffSides(String id, String left, String right) {
            when(sideRepository.getById(eq(DiffSide.LEFT), eq(id)))
                    .thenReturn(Optional.ofNullable(left));

            when(sideRepository.getById(eq(DiffSide.RIGHT), eq(id)))
                    .thenReturn(Optional.ofNullable(right));
        }
    }
}