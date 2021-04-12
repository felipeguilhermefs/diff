package com.ffdev.diff.helpers;

import com.ffdev.diff.domain.exceptions.InvalidBase64Exception;

import java.util.Base64;

public class Base64Helper {

    public static String encodeB64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String decodeB64(String data) {
        try {
            byte[] decoded = Base64.getDecoder().decode(data);
            return new String(decoded);
        } catch (IllegalArgumentException ex) {
            throw new InvalidBase64Exception();
        }
    }
}
