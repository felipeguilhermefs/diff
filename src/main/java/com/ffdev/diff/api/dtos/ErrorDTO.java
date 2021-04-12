package com.ffdev.diff.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ffdev.diff.api.enums.ErrorCode;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record ErrorDTO(ErrorCode code, String message) {
}
