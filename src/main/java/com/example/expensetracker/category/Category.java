package com.example.expensetracker.category;

import com.example.expensetracker.common.BusinessRuleViolationException;

import java.util.Objects;
import java.util.UUID;

public class Category {

    private final UUID id;
    private final String name;
    private final CategoryType type;

    public Category(String name, CategoryType type) {
        this.id = UUID.randomUUID();
        this.name = requireNotBlank(name);
        this.type = Objects.requireNonNull(type, "type must not be null");
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

    private static String requireNotBlank(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new BusinessRuleViolationException("category name must not be blank, but was: '" + name + "'");
        }
        return name.strip();
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
