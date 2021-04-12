package com.ffdev.diff.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record DiffResultDTO(String status, List<DiffChangeDTO> changes) {
}
