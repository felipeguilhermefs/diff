package com.ffdev.diff.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ffdev.diff.domain.models.Diff;

import java.util.List;

import static java.util.stream.Collectors.toList;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record DiffResponse(String result, List<Difference> differences) {

    public static DiffResponse from(Diff diff) {
        var differences = diff.differences()
                .stream()
                .map(d -> new Difference(d.offset(), d.length()))
                .collect(toList());

        return new DiffResponse(diff.result().name(), differences);
    }
}
