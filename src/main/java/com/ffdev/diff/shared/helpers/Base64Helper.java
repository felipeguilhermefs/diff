package com.ffdev.diff.shared.helpers;

import com.ffdev.diff.domain.exceptions.InvalidBase64Exception;

import java.util.Base64;

/**
 * {@link Base64Helper} static Base64 helper methods
 */
public class Base64Helper {

    /**
     * Encodes a string with base64
     *
     * @param data decoded string
     * @return encoded data
     */
    public static String encodeB64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    /**
     * Decodes a base64 string
     *
     * @param data encoded string
     * @return decoded data
     */
    public static String decodeB64(String data) {
        try {
            byte[] decoded = Base64.getDecoder().decode(data);
            return new String(decoded);
        } catch (IllegalArgumentException ex) {
            throw new InvalidBase64Exception();
        }
    }
}
