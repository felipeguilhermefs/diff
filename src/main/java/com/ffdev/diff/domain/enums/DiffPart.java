package com.ffdev.diff.domain.enums;

public enum DiffPart {
    LEFT("left"),
    RIGHT("right");

    private final String id;

    DiffPart(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
