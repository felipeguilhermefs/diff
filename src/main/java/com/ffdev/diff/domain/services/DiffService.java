package com.ffdev.diff.domain.services;

import com.ffdev.diff.domain.enums.DiffSide;
import com.ffdev.diff.domain.models.Diff;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
import com.ffdev.diff.domain.repositories.DiffSideRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DiffService {

    private final DiffSideRepository sideRepository;
    private final DiffCheckService checkService;

    public DiffService(DiffSideRepository sideRepository, DiffCheckService checkService) {
        this.sideRepository = sideRepository;
        this.checkService = checkService;
    }

    public void saveLeft(@NotNull String id, @NotNull String data) {
        save(DiffSide.LEFT, id, data);
    }

    public void saveRight(@NotNull String id, @NotNull String data) {
        save(DiffSide.RIGHT, id, data);
    }

    private void save(DiffSide side, String id, String data) {
        sideRepository.save(side, id, data);
    }

    public Diff getById(@NotNull String id) {
        String left = getSide(DiffSide.LEFT, id);
        String right = getSide(DiffSide.RIGHT, id);

        return checkService.getDiff(left, right);
    }

    private String getSide(DiffSide side, String id) {
        return sideRepository.getById(side, id)
                .orElseThrow(() -> new DiffSideNotFoundException(side));
    }
}