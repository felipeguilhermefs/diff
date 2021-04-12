package com.ffdev.diff.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JSONHelper {

    private static final JsonMapper jsonMapper = new JsonMapper();

    public static boolean isValidJSON(String data) {
        try {
            jsonMapper.readTree(data);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
