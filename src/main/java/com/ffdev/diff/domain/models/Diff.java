package com.ffdev.diff.domain.models;

import com.ffdev.diff.domain.enums.DiffResult;

import java.io.Serializable;
import java.util.List;

public record Diff(DiffResult result, List<Difference> differences) implements Serializable {
}
