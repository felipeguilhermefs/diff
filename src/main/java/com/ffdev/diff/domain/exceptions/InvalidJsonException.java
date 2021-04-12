package com.ffdev.diff.domain.exceptions;

public class InvalidJsonException extends CustomException {
    public InvalidJsonException() {
        super("Invalid JSON data");
    }
}
