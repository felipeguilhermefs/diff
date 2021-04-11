package com.ffdev.diff.controllers;

import com.ffdev.diff.helpers.PostDataProvider;
import com.ffdev.diff.helpers.RandomIdProvider;
import com.ffdev.diff.services.DiffCommandService;
import com.ffdev.diff.services.DiffQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DiffController.class)
@DisplayName("Diff Controller")
class DiffControllerTest {

    @MockBean
    private DiffCommandService commandService;

    @MockBean
    private DiffQueryService queryService;

    @Autowired
    private MockMvc mvc;

    @Nested
    @DisplayName("when saving left")
    class SaveLeft {

        @ParameterizedTest
        @ArgumentsSource(PostDataProvider.class)
        @DisplayName("should accept post data for given ID")
        public void shouldAccept(String id, String data) throws Exception {
            mvc.perform(
                    post("/v1/diff/{id}/left", id).content(data)
            ).andExpect(status().isAccepted());

            verify(commandService).saveLeft(eq(id), eq(data));
        }
    }

    @Nested
    @DisplayName("when saving right")
    class SaveRight {

        @ParameterizedTest
        @ArgumentsSource(PostDataProvider.class)
        @DisplayName("should accept post data for given ID")
        public void shouldAccept(String id, String data) throws Exception {
            mvc.perform(
                    post("/v1/diff/{id}/right", id).content(data)
            ).andExpect(status().isAccepted());

            verify(commandService).saveRight(eq(id), eq(data));
        }
    }

    @Nested
    @DisplayName("when retrieving diff")
    class GetDiff {

        @ParameterizedTest
        @ArgumentsSource(RandomIdProvider.class)
        @DisplayName("should return diff data for given ID")
        public void shouldReturnOk(String id) throws Exception {
            mvc.perform(
                    get("/v1/diff/{id}", id)
            ).andExpect(status().isOk());

            verify(queryService).getById(eq(id));
        }
    }
}