package com.ffdev.diff.services;

import com.ffdev.diff.domain.DiffPart;
import com.ffdev.diff.dtos.DiffChangeDTO;
import com.ffdev.diff.dtos.DiffResultDTO;
import com.ffdev.diff.exceptions.DiffPartNotFoundException;
import com.ffdev.diff.repositories.DiffPartRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class DiffQueryService {

    private final DiffPartRepository repository;

    public DiffQueryService(DiffPartRepository repository) {
        this.repository = repository;
    }

    public DiffResultDTO getById(@NotNull String id) {
        String left = getPart(DiffPart.LEFT, id);
        String right = getPart(DiffPart.RIGHT, id);

        if (left.length() != right.length()) {
            return new DiffResultDTO("DIFFERENT_SIZES", emptyList());
        }

        if (left.equals(right)) {
            return new DiffResultDTO("EQUAL", emptyList());
        }

        return new DiffResultDTO("DIFFERENT", getChanges(left, right));
    }

    private String getPart(DiffPart part, String id) {
        return repository.getById(part, id)
                .orElseThrow(() -> new DiffPartNotFoundException(part));
    }

    private List<DiffChangeDTO> getChanges(String left, String right) {
        List<DiffChangeDTO> changes = new ArrayList<>();
        long changeOffset = 0;
        boolean inChange = false;

        for (int offset = 0; offset < left.length(); offset++) {

            boolean isDifferent = left.charAt(offset) != right.charAt(offset);

            boolean startsChange = isDifferent && !inChange;
            if (startsChange) {
                changeOffset = offset;
                inChange = true;
                continue;
            }

            boolean endsChange = !isDifferent && inChange;
            if (endsChange) {
                changes.add(new DiffChangeDTO(changeOffset, offset - changeOffset));
                inChange = false;
            }
        }

        if (inChange) {
            changes.add(new DiffChangeDTO(changeOffset, left.length() - changeOffset));
        }

        return changes;
    }
}
