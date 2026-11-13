package com.example.expensetracker.category;

import java.util.Objects;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository, "categoryRepository must not be null");
    }

    public Category create(String name, CategoryType type) {
        Category category = new Category(name, type);
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new DuplicateCategoryNameException(category.getName());
        }
        return categoryRepository.save(category);
    }
}
