package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpenseFilterTest {

    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2027-01-01T00:00:00Z"), ZoneOffset.UTC);
    private static final LocalDate FROM = LocalDate.of(2026, 11, 1);
    private static final LocalDate TO = LocalDate.of(2026, 11, 30);

    @Test
    void constructor_shouldThrowException_whenFromIsAfterTo() {
        assertThatThrownBy(() -> new ExpenseFilter(TO, FROM, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("from must not be after to: 2026-11-30 > 2026-11-01");
    }

    @Test
    void matches_shouldAcceptAnyExpense_whenNoCriteriaAreSet() {
        ExpenseFilter filter = new ExpenseFilter(null, null, null);

        assertThat(filter.matches(expenseOn(FROM, FOOD))).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"2026-10-31, false", "2026-11-01, true", "2026-11-30, true", "2026-12-01, false"})
    void matches_shouldIncludePeriodBoundaries(LocalDate date, boolean expected) {
        ExpenseFilter filter = new ExpenseFilter(FROM, TO, null);

        assertThat(filter.matches(expenseOn(date, FOOD))).isEqualTo(expected);
    }

    @Test
    void matches_shouldRejectExpense_whenCategoryDiffers() {
        Category rent = new Category("Rent", CategoryType.FIXED);
        ExpenseFilter filter = new ExpenseFilter(null, null, FOOD.getId());

        assertThat(filter.matches(expenseOn(FROM, FOOD))).isTrue();
        assertThat(filter.matches(expenseOn(FROM, rent))).isFalse();
    }

    private static Expense expenseOn(LocalDate date, Category category) {
        return new Expense(Money.of("100.00", "UAH"), date, "Expense", category, CLOCK);
    }
}
