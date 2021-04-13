package com.ffdev.diff.domain.exceptions;

/**
 * {@link InvalidJsonException} thrown when a JSON string cannot be deserialized.
 */
public class InvalidJsonException extends ApplicationKnownException {
    public InvalidJsonException() {
        super("Invalid JSON data");
    }
}
