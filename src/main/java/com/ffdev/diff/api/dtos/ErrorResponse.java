package com.ffdev.diff.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ffdev.diff.api.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Schema(description = "Known error response")
@JsonAutoDetect(fieldVisibility = ANY)
public record ErrorResponse(
        @NotNull ErrorCode code,
        @Schema(description = "Human readable error message")
        @NotNull String message
) {
}
