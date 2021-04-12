package com.ffdev.diff.domain.services;

import com.ffdev.diff.domain.models.Diff;
import com.ffdev.diff.domain.models.Difference;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ffdev.diff.domain.enums.DiffResult.*;
import static java.util.Collections.emptyList;

@Service
public class DiffCheckService {

    /**
     * getDiff linearly compares two strings and returns information of side-by-side differences
     */
    public Diff getDiff(@NotNull String left, @NotNull String right) {

        if (left.length() != right.length()) {
            return new Diff(DIFFERENT_SIZES, emptyList());
        }

        if (left.equals(right)) {
            return new Diff(EQUAL, emptyList());
        }

        return new Diff(DIFFERENT, getChanges(left, right));
    }

    private List<Difference> getChanges(String left, String right) {
        List<Difference> differences = new ArrayList<>();
        long diffOffset = 0;
        boolean currentlyInDiff = false;

        for (int offset = 0; offset < left.length(); offset++) {

            boolean isDifferent = left.charAt(offset) != right.charAt(offset);

            boolean beginsDiff = isDifferent && !currentlyInDiff;
            if (beginsDiff) {
                diffOffset = offset;
                currentlyInDiff = true;
                continue;
            }

            boolean endsDiff = !isDifferent && currentlyInDiff;
            if (endsDiff) {
                differences.add(new Difference(diffOffset, offset - diffOffset));
                currentlyInDiff = false;
            }
        }

        if (currentlyInDiff) {
            differences.add(new Difference(diffOffset, left.length() - diffOffset));
        }

        return differences;
    }
}