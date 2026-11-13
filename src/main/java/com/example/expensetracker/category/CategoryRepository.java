package com.example.expensetracker.category;

public interface CategoryRepository {

    /**
     * Compares names ignoring case, so "Food" and "food" are the same category.
     */
    boolean existsByNameIgnoreCase(String name);

    Category save(Category category);
}
