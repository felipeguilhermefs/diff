package com.ffdev.diff.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record Difference(long offset, long length) implements Serializable {
}
