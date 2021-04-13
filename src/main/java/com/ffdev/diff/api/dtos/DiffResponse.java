package com.ffdev.diff.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ffdev.diff.api.enums.DiffResult;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record DiffResponse(@NotNull DiffResult result, @NotNull List<Difference> differences) implements Serializable {
}
