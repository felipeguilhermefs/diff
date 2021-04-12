package com.ffdev.diff.dtos;

public class DiffChangeDTO {
    private final String action;
    private final Long offset;
    private final Long length;

    public DiffChangeDTO(String action, Long offset, Long length) {
        this.action = action;
        this.offset = offset;
        this.length = length;
    }

    public String getAction() {
        return action;
    }

    public Long getOffset() {
        return offset;
    }

    public Long getLength() {
        return length;
    }
}
