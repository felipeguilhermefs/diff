package com.ffdev.diff.shared.helpers;

import static java.util.UUID.randomUUID;

/**
 * {@link RandomHelper} static random helper methods.
 */
public class RandomHelper {

    /**
     * Provides a random UUID string.
     *
     * @return random uuid string
     */
    public static String uuid() {
        return randomUUID().toString();
    }
}
