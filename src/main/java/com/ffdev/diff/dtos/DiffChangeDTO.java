package com.ffdev.diff.dtos;

public class DiffChangeDTO {
    private final Long offset;
    private final Long length;

    public DiffChangeDTO(Long offset, Long length) {
        this.offset = offset;
        this.length = length;
    }

    public Long getOffset() {
        return offset;
    }

    public Long getLength() {
        return length;
    }
}
