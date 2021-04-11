package com.ffdev.diff.helpers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PostDataProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                arguments(generateRandom(), "{\"id\":123,\"message\":\"some json\"}"),
                arguments(generateRandom(), "some plain text"),
                arguments(generateRandom(), "<html><head></head><body><h1>some html</h1></body></html>")
        );
    }

    private String generateRandom() {
        return UUID.randomUUID().toString();
    }
}
