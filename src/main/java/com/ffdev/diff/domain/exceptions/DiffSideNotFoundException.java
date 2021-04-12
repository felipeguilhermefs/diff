package com.ffdev.diff.domain.exceptions;

import com.ffdev.diff.domain.enums.DiffSide;

public class DiffSideNotFoundException extends RuntimeException {

    public DiffSideNotFoundException(DiffSide side) {
        super(String.format("Diff %s side was not found", side.getId()));
    }
}
