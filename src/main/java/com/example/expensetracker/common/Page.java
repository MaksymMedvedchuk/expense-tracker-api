package com.example.expensetracker.common;

import java.util.List;
import java.util.Objects;

/**
 * One page of results plus the total count of matching elements, independent of Spring Data.
 */
public record Page<T>(List<T> content, PageRequest request, long totalElements) {

    public Page {
        content = List.copyOf(content);
        Objects.requireNonNull(request, "request must not be null");
        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements must not be negative, but was: " + totalElements);
        }
    }

    public long totalPages() {
        return Math.ceilDiv(totalElements, request.size());
    }
}
