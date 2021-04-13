package com.ffdev.diff.domain.models;

import com.ffdev.diff.domain.enums.DiffResult;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public record Diff(@NotNull DiffResult result, @NotNull List<Difference> differences) implements Serializable {
}
