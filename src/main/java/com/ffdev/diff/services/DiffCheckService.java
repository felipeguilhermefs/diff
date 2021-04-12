package com.ffdev.diff.services;

import com.ffdev.diff.dtos.DiffChangeDTO;
import com.ffdev.diff.dtos.DiffResultDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class DiffCheckService {

    /**
     * getDiff linearly compares two strings and returns information of side-by-side differences
     */
    public DiffResultDTO getDiff(@NotNull String left, @NotNull String right) {

        if (left.length() != right.length()) {
            return new DiffResultDTO("DIFFERENT_SIZES", emptyList());
        }

        if (left.equals(right)) {
            return new DiffResultDTO("EQUAL", emptyList());
        }

        return new DiffResultDTO("DIFFERENT", getChanges(left, right));
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
