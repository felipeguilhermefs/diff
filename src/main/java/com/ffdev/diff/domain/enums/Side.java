package com.ffdev.diff.domain.enums;

public enum Side {
    LEFT("left"),
    RIGHT("right");

    private final String id;

    Side(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
