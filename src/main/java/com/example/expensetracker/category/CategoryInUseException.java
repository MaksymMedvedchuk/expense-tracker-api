package com.example.expensetracker.category;

import com.example.expensetracker.common.DomainException;

import java.util.UUID;

public class CategoryInUseException extends DomainException {

    public CategoryInUseException(UUID id) {
        super("category with id '" + id + "' is used by expenses");
    }
}
