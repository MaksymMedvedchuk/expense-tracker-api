package com.example.expensetracker.expense;

import java.time.LocalDate;
import java.util.List;

public class InMemoryExpenseRepository implements ExpenseRepository {

    private final List<Expense> expenses;

    public InMemoryExpenseRepository(List<Expense> expenses) {
        this.expenses = List.copyOf(expenses);
    }

    @Override
    public List<Expense> findBetween(LocalDate from, LocalDate to) {
        return expenses.stream()
                .filter(expense -> isWithin(expense.getDate(), from, to))
                .toList();
    }

    private static boolean isWithin(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
