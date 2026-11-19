package com.example.expensetracker.expense;

import com.example.expensetracker.common.DomainException;

import java.util.UUID;

public class ExpenseNotFoundException extends DomainException {

    public ExpenseNotFoundException(UUID id) {
        super("expense with id '" + id + "' not found");
    }
}
