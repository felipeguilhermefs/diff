package com.ffdev.diff.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record DiffChangeDTO(Long offset, Long length) {
}
