package com.ffdev.diff.domain.exceptions;

import com.ffdev.diff.domain.enums.Side;

/**
 * {@link DiffSideNotFoundException} thrown when a diff side is not found.
 */
public class DiffSideNotFoundException extends ApplicationKnownException {

    private final Side side;

    public DiffSideNotFoundException(Side side) {
        super(String.format("Diff %s side was not found", side.getId()));
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
