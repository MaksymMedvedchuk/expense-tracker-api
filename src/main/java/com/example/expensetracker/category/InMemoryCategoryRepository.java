package com.example.expensetracker.category;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Temporary storage until the database is added; data is lost on restart.
 */
@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<UUID, Category> categories = new ConcurrentHashMap<>();

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return categories.values().stream()
                .anyMatch(category -> category.getName().equalsIgnoreCase(name));
    }

    @Override
    public Category save(Category category) {
        categories.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public List<Category> findAll() {
        return categories.values().stream()
                .sorted(Comparator.comparing(Category::getName))
                .toList();
    }
}
