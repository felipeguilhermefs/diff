package com.ffdev.diff.domain.entities;

import com.ffdev.diff.domain.enums.Side;
import org.jetbrains.annotations.NotNull;

/**
 * {@link DiffSide} entity that holds data related to a diff side.
 *
 * @param side   which side
 * @param diffId diff identifier
 * @param data   data that will be diffed
 */
public record DiffSide(@NotNull Side side, @NotNull String diffId, @NotNull String data) {
}
