package com.example.expensetracker.category;

import com.example.expensetracker.common.DomainException;

public class DuplicateCategoryNameException extends DomainException {

    public DuplicateCategoryNameException(String name) {
        super("category with name '" + name + "' already exists");
    }
}
