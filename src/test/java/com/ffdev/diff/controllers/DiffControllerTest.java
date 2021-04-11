package com.ffdev.diff.controllers;

import com.ffdev.diff.helpers.PostDataProvider;
import com.ffdev.diff.services.DiffCommandService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DiffController.class)
@DisplayName("UT: Diff API")
class DiffControllerTest {

    @MockBean
    private DiffCommandService commandService;

    @Autowired
    private MockMvc mvc;

    @Nested
    @DisplayName("when saving left")
    class SaveLeft {

        @ParameterizedTest
        @ArgumentsSource(PostDataProvider.class)
        @DisplayName("should accept post data for given ID")
        public void shouldAccept(String id, byte[] data) throws Exception {
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
        public void shouldAccept(String id, byte[] data) throws Exception {
            mvc.perform(
                    post("/v1/diff/{id}/right", id).content(data)
            ).andExpect(status().isAccepted());

            verify(commandService).saveRight(eq(id), eq(data));
        }
    }
}