package com.example.expensetracker.expense;

import com.example.expensetracker.category.CategoryUsage;

import java.util.Objects;
import java.util.UUID;

/**
 * Answers {@link CategoryUsage} from stored expenses: a category is in use while any expense references it.
 */
class ExpenseCategoryUsage implements CategoryUsage {

    private final ExpenseRepository expenseRepository;

    ExpenseCategoryUsage(ExpenseRepository expenseRepository) {
        this.expenseRepository = Objects.requireNonNull(expenseRepository, "expenseRepository must not be null");
    }

    @Override
    public boolean isInUse(UUID categoryId) {
        return expenseRepository.existsByCategoryId(categoryId);
    }
}
