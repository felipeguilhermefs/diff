package com.ffdev.diff.domain.enums;

public enum DiffSide {
    LEFT("left"),
    RIGHT("right");

    private final String id;

    DiffSide(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
