package com.example.expensetracker.expense;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Optional criteria for listing expenses; a {@code null} field means "do not filter by it".
 * Period boundaries are inclusive.
 */
public record ExpenseFilter(LocalDate from, LocalDate to, UUID categoryId) {

    public ExpenseFilter {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from must not be after to: " + from + " > " + to);
        }
    }

    public boolean matches(Expense expense) {
        LocalDate date = expense.getDate();
        return (from == null || !date.isBefore(from))
                && (to == null || !date.isAfter(to))
                && (categoryId == null || categoryId.equals(expense.getCategory().getId()));
    }
}
