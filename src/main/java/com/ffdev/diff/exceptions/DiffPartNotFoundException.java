package com.ffdev.diff.exceptions;

import com.ffdev.diff.domain.DiffPart;

public class DiffPartNotFoundException extends RuntimeException {

    public DiffPartNotFoundException(DiffPart part) {
        super(String.format("Diff %s part was not found ", part.getId()));
    }
}
