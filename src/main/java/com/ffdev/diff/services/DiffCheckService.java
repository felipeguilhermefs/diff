package com.ffdev.diff.services;

import com.ffdev.diff.api.dtos.DifferenceDTO;
import com.ffdev.diff.api.dtos.DiffResonseDTO;
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
    public DiffResonseDTO getDiff(@NotNull String left, @NotNull String right) {

        if (left.length() != right.length()) {
            return new DiffResonseDTO("DIFFERENT_SIZES", emptyList());
        }

        if (left.equals(right)) {
            return new DiffResonseDTO("EQUAL", emptyList());
        }

        return new DiffResonseDTO("DIFFERENT", getChanges(left, right));
    }

    private List<DifferenceDTO> getChanges(String left, String right) {
        List<DifferenceDTO> changes = new ArrayList<>();
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
                changes.add(new DifferenceDTO(changeOffset, offset - changeOffset));
                inChange = false;
            }
        }

        if (inChange) {
            changes.add(new DifferenceDTO(changeOffset, left.length() - changeOffset));
        }

        return changes;
    }
}
