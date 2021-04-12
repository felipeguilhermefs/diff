package com.ffdev.diff.services;

import com.ffdev.diff.domain.enums.DiffPart;
import com.ffdev.diff.domain.models.Diff;
import com.ffdev.diff.exceptions.DiffPartNotFoundException;
import com.ffdev.diff.repositories.DiffPartRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DiffService {

    private final DiffPartRepository repository;
    private final DiffCheckService checkService;

    public DiffService(DiffPartRepository repository, DiffCheckService checkService) {
        this.repository = repository;
        this.checkService = checkService;
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

    public Diff getById(@NotNull String id) {
        String left = getPart(DiffPart.LEFT, id);
        String right = getPart(DiffPart.RIGHT, id);

        return checkService.getDiff(left, right);
    }

    private String getPart(DiffPart part, String id) {
        return repository.getById(part, id)
                .orElseThrow(() -> new DiffPartNotFoundException(part));
    }
}
