package com.ffdev.diff.services;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.repositories.DiffWriteRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DiffCommandService {

    private final DiffWriteRepository repository;

    public DiffCommandService(DiffWriteRepository repository) {
        this.repository = repository;
    }

    public void saveLeft(@NotNull String id, @NotNull String data) {
        save(id, data, DiffPart.LEFT);
    }

    public void saveRight(@NotNull String id, @NotNull String data) {
        save(id, data, DiffPart.RIGHT);
    }

    private void save(String id, String data, DiffPart part) {
        repository.savePart(id, data, part);
    }
}
