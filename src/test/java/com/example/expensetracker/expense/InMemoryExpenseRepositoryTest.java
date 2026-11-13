package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryExpenseRepositoryTest {

    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2027-01-01T00:00:00Z"), ZoneOffset.UTC);

    private static final LocalDate FROM = LocalDate.of(2026, 11, 1);
    private static final LocalDate TO = LocalDate.of(2026, 11, 30);

    @Test
    void findBetween_shouldReturnEmptyList_whenThereAreNoExpenses() {
        InMemoryExpenseRepository repository = new InMemoryExpenseRepository(List.of());

        List<Expense> found = repository.findBetween(FROM, TO);

        assertThat(found).isEmpty();
    }

    @Test
    void findBetween_shouldReturnOnlyExpensesWithinPeriodIncludingBoundaries() {
        Expense before = expenseOn(FROM.minusDays(1));
        Expense firstDay = expenseOn(FROM);
        Expense middle = expenseOn(LocalDate.of(2026, 11, 15));
        Expense lastDay = expenseOn(TO);
        Expense after = expenseOn(TO.plusDays(1));
        InMemoryExpenseRepository repository = new InMemoryExpenseRepository(
                List.of(before, firstDay, middle, lastDay, after));

        List<Expense> found = repository.findBetween(FROM, TO);

        assertThat(found).containsExactly(firstDay, middle, lastDay);
    }

    private static Expense expenseOn(LocalDate date) {
        return new Expense(Money.of("100.00", "UAH"), date, "Expense", FOOD, CLOCK);
    }
}
