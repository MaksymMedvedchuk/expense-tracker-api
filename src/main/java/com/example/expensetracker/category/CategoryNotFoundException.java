package com.example.expensetracker.category;

import com.example.expensetracker.common.DomainException;

import java.util.UUID;

public class CategoryNotFoundException extends DomainException {

    public CategoryNotFoundException(UUID id) {
        super("category with id '" + id + "' not found");
    }
}
