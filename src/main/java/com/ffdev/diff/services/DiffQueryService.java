package com.ffdev.diff.services;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.dtos.DiffChangeDTO;
import com.ffdev.diff.dtos.DiffResultDTO;
import com.ffdev.diff.exceptions.DiffPartNotFoundException;
import com.ffdev.diff.repositories.DiffPartRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Service
public class DiffQueryService {

    private final DiffPartRepository repository;

    public DiffQueryService(DiffPartRepository repository) {
        this.repository = repository;
    }

    public DiffResultDTO getById(@NotNull String id) {
        String leftPart = getPart(DiffPart.LEFT, id);
        String rightPart = getPart(DiffPart.RIGHT, id);

        if (leftPart.length() != rightPart.length()) {
            return buildDifferentSizes();
        }

        List<DiffChangeDTO> changes = singletonList(new DiffChangeDTO("some-action", 0L, (long) leftPart.length()));
        return new DiffResultDTO("some-result-status", changes);
    }

    private String getPart(DiffPart part, String id) {
        return repository.getById(part, id)
                .orElseThrow(() -> new DiffPartNotFoundException(part));
    }

    private DiffResultDTO buildDifferentSizes() {
        return new DiffResultDTO("DIFFERENT_SIZES", emptyList());
    }
}
