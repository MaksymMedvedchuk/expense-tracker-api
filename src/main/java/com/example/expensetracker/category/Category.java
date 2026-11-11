package com.example.expensetracker.category;

import com.example.expensetracker.common.BusinessRuleViolationException;

import java.util.Objects;

public class Category {

    private final String name;
    private final CategoryType type;

    public Category(String name, CategoryType type) {
        this.name = requireNotBlank(name);
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    private static String requireNotBlank(String name) {
        if (name == null || name.isBlank()) {
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
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
