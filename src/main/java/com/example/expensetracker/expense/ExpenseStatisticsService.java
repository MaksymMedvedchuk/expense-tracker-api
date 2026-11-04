package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.common.Money;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
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
        return totalFor(from, to).map(total -> total.divide(daysInclusive(from, to)));
    }

    private Stream<Expense> expensesWithin(LocalDate from, LocalDate to) {
        requireValidPeriod(from, to);
        List<Expense> within = expenses.stream()
                .filter(expense -> isWithin(expense.getDate(), from, to))
                .toList();
        requireSingleCurrency(within);
        return within.stream();
    }

    private static void requireSingleCurrency(List<Expense> expenses) {
        List<Currency> currencies = expenses.stream()
                .map(expense -> expense.getAmount().currency())
                .distinct()
                .toList();
        if (currencies.size() > 1) {
            throw new IllegalArgumentException(
                    "currencies must match: " + currencies.get(0) + " and " + currencies.get(1));
        }
    }

    private static void requireValidPeriod(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from must not be after to: " + from + " > " + to);
        }
    }

    private static long daysInclusive(LocalDate from, LocalDate to) {
        return ChronoUnit.DAYS.between(from, to) + 1;
    }

    private static boolean isWithin(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
