package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void totalForShouldSumOnlyExpensesWithinPeriodIncludingBoundaries() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of(
                expenseOn(FROM.minusDays(1), "1000.00"),
                expenseOn(FROM, "100.00"),
                expenseOn(LocalDate.of(2026, 11, 15), "20.00"),
                expenseOn(TO, "3.00"),
                expenseOn(TO.plusDays(1), "5000.00")));

        Optional<Money> total = service.totalFor(FROM, TO);

        assertThat(total).contains(Money.of("123.00", "UAH"));
    }

    @Test
    void totalForShouldThrowExceptionWhenFromIsAfterTo() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of());

        assertThatThrownBy(() -> service.totalFor(TO, FROM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("from must not be after to: 2026-11-30 > 2026-11-01");
    }

    @Test
    void totalForShouldThrowExceptionWhenExpensesHaveDifferentCurrencies() {
        Expense inHryvnias = new Expense(Money.of("100.00", "UAH"), FROM, "Taxi", new Category());
        Expense inDollars = new Expense(Money.of("10.00", "USD"), TO, "Book", new Category());
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of(inHryvnias, inDollars));

        assertThatThrownBy(() -> service.totalFor(FROM, TO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("currencies must match: UAH and USD");
    }

    @Test
    void totalsByCategoryShouldReturnEmptyMapWhenThereAreNoExpenses() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of());

        Map<Category, Money> totals = service.totalsByCategory(FROM, TO);

        assertThat(totals).isEmpty();
    }

    private static Expense expenseOn(LocalDate date, String amount) {
        return new Expense(Money.of(amount, "UAH"), date, "Expense", new Category());
    }
}
