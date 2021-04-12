package com.ffdev.diff.domain.models;

import com.ffdev.diff.domain.enums.DiffResult;

import java.util.List;

public record Diff(DiffResult result, List<Difference> differences) {
}
