package com.example.expensetracker.category;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category update(UUID id, String name, CategoryType type) {
        Category category = findById(id);
        String normalizedName = Category.normalizeName(name);
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new DuplicateCategoryNameException(normalizedName);
        }
        category.update(normalizedName, type);
        return categoryRepository.save(category);
    }

    public void delete(UUID id) {
        findById(id);
        categoryRepository.deleteById(id);
    }
}
