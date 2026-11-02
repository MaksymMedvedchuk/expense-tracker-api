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

    @Test
    void totalsByCategoryShouldSumExpensesPerCategoryWhenCategoriesAreEqualByName() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of(
                expenseIn("Food", "150.00"),
                expenseIn("Transport", "200.00"),
                expenseIn("Food", "300.00")));

        Map<Category, Money> totals = service.totalsByCategory(FROM, TO);

        assertThat(totals).containsExactlyInAnyOrderEntriesOf(Map.of(
                categoryNamed("Food"), Money.of("450.00", "UAH"),
                categoryNamed("Transport"), Money.of("200.00", "UAH")));
    }

    @Test
    void totalsByCategoryShouldIgnoreExpensesOutsidePeriod() {
        Category food = categoryNamed("Food");
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of(
                new Expense(Money.of("1000.00", "UAH"), FROM.minusDays(1), "Before", food),
                new Expense(Money.of("100.00", "UAH"), FROM, "First day", food),
                new Expense(Money.of("20.00", "UAH"), TO, "Last day", food),
                new Expense(Money.of("5000.00", "UAH"), TO.plusDays(1), "After", categoryNamed("Travel"))));

        Map<Category, Money> totals = service.totalsByCategory(FROM, TO);

        assertThat(totals).containsExactlyEntriesOf(Map.of(food, Money.of("120.00", "UAH")));
    }

    @Test
    void totalsByCategoryShouldThrowExceptionWhenFromIsAfterTo() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of());

        assertThatThrownBy(() -> service.totalsByCategory(TO, FROM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("from must not be after to: 2026-11-30 > 2026-11-01");
    }

    @Test
    void averagePerDayShouldReturnEmptyWhenThereAreNoExpenses() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of());

        Optional<Money> average = service.averagePerDay(FROM, TO);

        assertThat(average).isEmpty();
    }

    @Test
    void averagePerDayShouldDivideTotalByAllDaysInPeriodIncludingDaysWithoutExpenses() {
        LocalDate tenthDay = FROM.plusDays(9);
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of(
                expenseOn(FROM, "400.00"),
                expenseOn(tenthDay, "600.00")));

        Optional<Money> average = service.averagePerDay(FROM, tenthDay);

        assertThat(average).contains(Money.of("100.00", "UAH"));
    }

    @Test
    void averagePerDayShouldThrowExceptionWhenFromIsAfterTo() {
        ExpenseStatisticsService service = new ExpenseStatisticsService(List.of());

        assertThatThrownBy(() -> service.averagePerDay(TO, FROM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("from must not be after to: 2026-11-30 > 2026-11-01");
    }

    private static Expense expenseOn(LocalDate date, String amount) {
        return new Expense(Money.of(amount, "UAH"), date, "Expense", new Category());
    }

    private static Expense expenseIn(String categoryName, String amount) {
        return new Expense(Money.of(amount, "UAH"), FROM, "Expense", categoryNamed(categoryName));
    }

    private static Category categoryNamed(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
