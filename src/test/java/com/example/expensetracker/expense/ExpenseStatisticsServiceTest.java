package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenseStatisticsServiceTest {

    private static final LocalDate FROM = LocalDate.of(2026, 11, 1);
    private static final LocalDate TO = LocalDate.of(2026, 11, 30);

    @Test
    void totalForShouldReturnEmptyWhenThereAreNoExpenses() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of());

        Optional<Money> total = service.totalFor(FROM, TO);

        assertThat(total).isEmpty();
    }

    @Test
    void totalForShouldReturnAmountWhenThereIsOneExpenseInPeriod() {
        Money amount = Money.of("150.00", "UAH");
        Expense expense = new Expense(amount, LocalDate.of(2026, 11, 15), "Lunch", new Category());
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of(expense));

        Optional<Money> total = service.totalFor(FROM, TO);

        assertThat(total).contains(amount);
    }
}
