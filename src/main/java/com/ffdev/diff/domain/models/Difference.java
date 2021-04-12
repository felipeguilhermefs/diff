package com.ffdev.diff.domain.models;

import java.io.Serializable;

public record Difference(Long offset, Long length) implements Serializable {
}
