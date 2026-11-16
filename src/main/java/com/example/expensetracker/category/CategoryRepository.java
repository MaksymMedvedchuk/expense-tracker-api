package com.example.expensetracker.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    /**
     * Compares names ignoring case, so "Food" and "food" are the same category.
     */
    boolean existsByNameIgnoreCase(String name);

    Category save(Category category);

    Optional<Category> findById(UUID id);

    /**
     * Returns categories sorted by name.
     */
    List<Category> findAll();
}
