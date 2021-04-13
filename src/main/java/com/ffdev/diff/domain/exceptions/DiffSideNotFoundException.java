package com.ffdev.diff.domain.exceptions;

import com.ffdev.diff.domain.enums.Side;

public class DiffSideNotFoundException extends CustomException {

    private final Side side;

    public DiffSideNotFoundException(Side side) {
        super(String.format("Diff %s side was not found", side.getId()));
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
