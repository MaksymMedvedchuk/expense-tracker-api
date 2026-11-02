package com.example.expensetracker.expense;

import com.example.expensetracker.common.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ExpenseStatisticsService {

    private final List<Expense> expenses;

    public ExpenseStatisticsService(List<Expense> expenses) {
        this.expenses = List.copyOf(expenses);
    }

    public Optional<Money> totalFor(LocalDate from, LocalDate to) {
        if (expenses.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(expenses.get(0).getAmount());
    }
}
