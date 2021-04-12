package com.ffdev.diff.domain.exceptions;

public class InvalidBase64Exception extends CustomException {
    public InvalidBase64Exception() {
        super("Invalid base 64 data");
    }
}
