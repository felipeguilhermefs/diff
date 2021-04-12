package com.ffdev.diff.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ffdev.diff.domain.models.Diff;

import java.util.List;

import static java.util.stream.Collectors.toList;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record ResponseDTO(String result, List<DifferenceDTO> differences) {

    public static ResponseDTO from(Diff diff) {
        List<DifferenceDTO> differences = diff.differences()
                .stream()
                .map(d -> new DifferenceDTO(d.offset(), d.length()))
                .collect(toList());

        return new ResponseDTO(diff.result().name(), differences);
    }
}
