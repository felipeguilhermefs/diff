package com.ffdev.diff.domain.exceptions;

import com.ffdev.diff.domain.enums.DiffSide;

public class DiffSideNotFoundException extends RuntimeException {

    private final DiffSide side;

    public DiffSideNotFoundException(DiffSide side) {
        super(String.format("Diff %s side was not found", side.getId()));
        this.side = side;
    }

    public DiffSide getSide() {
        return side;
    }
}
