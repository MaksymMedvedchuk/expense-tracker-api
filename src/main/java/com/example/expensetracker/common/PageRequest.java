package com.example.expensetracker.common;

/**
 * Zero-based page number and page size requested by the client.
 */
public record PageRequest(int page, int size) {

    public PageRequest {
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative, but was: " + page);
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive, but was: " + size);
        }
    }

    public long offset() {
        return (long) page * size;
    }
}
