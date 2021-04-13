package com.ffdev.diff.domain.models;

import com.ffdev.diff.domain.enums.Side;
import org.jetbrains.annotations.NotNull;

public record DiffSide(@NotNull Side side, @NotNull String id, @NotNull String data) {
}
