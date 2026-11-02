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
        return expenses.stream()
                .filter(expense -> isWithin(expense.getDate(), from, to))
                .map(Expense::getAmount)
                .reduce(Money::add);
    }

    private static boolean isWithin(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
