package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenseCategoryUsageTest {

    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);
    private static final Category RENT = new Category("Rent", CategoryType.FIXED);
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-11-20T10:00:00Z"), ZoneOffset.UTC);

    private final InMemoryExpenseRepository expenseRepository = new InMemoryExpenseRepository();
    private final ExpenseCategoryUsage categoryUsage = new ExpenseCategoryUsage(expenseRepository);

    @Test
    void isInUse_shouldReturnTrue_whenCategoryHasExpenses() {
        expenseRepository.save(expenseIn(FOOD));

        boolean inUse = categoryUsage.isInUse(FOOD.getId());

        assertThat(inUse).isTrue();
    }

    @Test
    void isInUse_shouldReturnFalse_whenCategoryHasNoExpenses() {
        expenseRepository.save(expenseIn(RENT));

        boolean inUse = categoryUsage.isInUse(FOOD.getId());

        assertThat(inUse).isFalse();
    }

    private static Expense expenseIn(Category category) {
        return new Expense(Money.of("100.00", "UAH"), LocalDate.of(2026, 11, 15), "Expense", category, CLOCK);
    }
}
