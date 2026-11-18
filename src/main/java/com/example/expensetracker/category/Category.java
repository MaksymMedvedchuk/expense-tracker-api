package com.example.expensetracker.category;

import com.example.expensetracker.common.BusinessRuleViolationException;

import java.util.Objects;
import java.util.UUID;

public class Category {

    private final UUID id;
    private String name;
    private CategoryType type;

    public Category(String name, CategoryType type) {
        this.id = UUID.randomUUID();
        this.name = normalizeName(name);
        this.type = requireType(type);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    /**
     * Replaces name and type together; both are validated before any field changes,
     * so a failed update leaves the category untouched.
     */
    public void update(String newName, CategoryType newType) {
        String validName = normalizeName(newName);
        CategoryType validType = requireType(newType);
        this.name = validName;
        this.type = validType;
    }

    /**
     * Validates and strips a category name; shared with the service to check uniqueness before updating.
     */
    static String normalizeName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new BusinessRuleViolationException("category name must not be blank, but was: '" + name + "'");
        }
        return name.strip();
    }

    private static CategoryType requireType(CategoryType type) {
        return Objects.requireNonNull(type, "type must not be null");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Category other)) {
            return false;
        }
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
