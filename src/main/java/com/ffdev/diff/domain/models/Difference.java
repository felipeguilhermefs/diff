package com.ffdev.diff.domain.models;

import java.io.Serializable;

public record Difference(long offset, long length) implements Serializable {
}
