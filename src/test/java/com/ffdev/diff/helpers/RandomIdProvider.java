package com.ffdev.diff.helpers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RandomIdProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                arguments(generateRandom()),
                arguments(generateRandom()),
                arguments(generateRandom())
        );
    }

    private String generateRandom() {
        return UUID.randomUUID().toString();
    }
}
