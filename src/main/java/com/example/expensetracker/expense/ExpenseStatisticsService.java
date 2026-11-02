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
        Money total = null;
        for (Expense expense : expenses) {
            LocalDate date = expense.getDate();
            if (!date.isBefore(from) && !date.isAfter(to)) {
                if (total == null) {
                    total = expense.getAmount();
                } else {
                    total = total.add(expense.getAmount());
                }
            }
        }
        return Optional.ofNullable(total);
    }
}
