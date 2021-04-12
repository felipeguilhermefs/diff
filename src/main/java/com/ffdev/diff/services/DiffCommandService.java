package com.ffdev.diff.services;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.repositories.DiffPartRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DiffCommandService {

    private final DiffPartRepository repository;

    public DiffCommandService(DiffPartRepository repository) {
        this.repository = repository;
    }

    public void saveLeft(@NotNull String id, @NotNull String data) {
        save(DiffPart.LEFT, id, data);
    }

    public void saveRight(@NotNull String id, @NotNull String data) {
        save(DiffPart.RIGHT, id, data);
    }

    private void save(DiffPart part, String id, String data) {
        repository.save(part, id, data);
    }
}
