package com.ffdev.diff.dtos;

import java.util.List;

public class DiffResultDTO {

    private final String status;
    private final List<DiffChangeDTO> changes;

    public DiffResultDTO(String status, List<DiffChangeDTO> changes) {
        this.status = status;
        this.changes = changes;
    }

    public String getStatus() {
        return status;
    }

    public List<DiffChangeDTO> getChanges() {
        return changes;
    }
}
