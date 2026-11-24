package com.example.expensetracker.common.web;

import com.example.expensetracker.common.Page;

import java.util.List;
import java.util.function.Function;

/**
 * JSON representation of a {@link Page}, so the domain type does not leak into the HTTP API.
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, long totalPages) {

    /**
     * Converts a domain page, mapping each element with the given function.
     */
    public static <S, T> PageResponse<T> from(Page<S> page, Function<S, T> mapper) {
        return new PageResponse<>(
                page.content().stream().map(mapper).toList(),
                page.request().page(),
                page.request().size(),
                page.totalElements(),
                page.totalPages());
    }
}
