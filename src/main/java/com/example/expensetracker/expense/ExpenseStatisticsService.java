package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.common.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpenseStatisticsService {

    private final List<Expense> expenses;

    public ExpenseStatisticsService(List<Expense> expenses) {
        this.expenses = List.copyOf(expenses);
    }

    public Optional<Money> totalFor(LocalDate from, LocalDate to) {
        return expensesWithin(from, to)
                .map(Expense::getAmount)
                .reduce(Money::add);
    }

    public Map<Category, Money> totalsByCategory(LocalDate from, LocalDate to) {
        return expensesWithin(from, to)
                .collect(Collectors.toUnmodifiableMap(Expense::getCategory, Expense::getAmount, Money::add));
    }

    public Optional<Money> averagePerDay(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    private Stream<Expense> expensesWithin(LocalDate from, LocalDate to) {
        requireValidPeriod(from, to);
        return expenses.stream()
                .filter(expense -> isWithin(expense.getDate(), from, to));
    }

    private static void requireValidPeriod(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from must not be after to: " + from + " > " + to);
        }
    }

    private static boolean isWithin(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
