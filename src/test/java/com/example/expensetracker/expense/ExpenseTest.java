package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.BusinessRuleViolationException;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpenseTest {

    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);
    private static final Money AMOUNT = Money.of("12.50", "UAH");
    private static final LocalDate DATE = LocalDate.of(2026, 10, 1);
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 31);
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-10-31T10:00:00Z"), ZoneOffset.UTC);

    @Test
    void constructor_shouldCreateExpense_whenRequiredFieldsAreProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);

        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
        assertThat(expense.getDate()).isEqualTo(DATE);
        assertThat(expense.getDescription()).isEqualTo("Coffee");
        assertThat(expense.getCategory()).isSameAs(FOOD);
    }

    @Test
    void constructor_shouldCreateExpense_whenDescriptionIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, null, FOOD, CLOCK);

        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void constructor_shouldThrowException_whenAmountIsNull() {
        assertThatThrownBy(() -> new Expense(null, DATE, "Coffee", FOOD, CLOCK))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0.00",
        "0.00, 0.00",
        "0.001, 0.00",
        "0.005, 0.00",
        "-0.01, -0.01",
        "-100, -100.00"
    })
    void constructor_shouldThrowException_whenAmountIsNotPositive(String amount, String roundedAmount) {
        Money notPositive = Money.of(amount, "UAH");

        assertThatThrownBy(() -> new Expense(notPositive, DATE, "Coffee", FOOD, CLOCK))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("expense amount must be positive, but was: " + roundedAmount + " UAH");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.01", "0.006", "1.00", "1000000.00"})
    void constructor_shouldCreateExpense_whenAmountIsPositive(String amount) {
        Money positive = Money.of(amount, "UAH");

        Expense expense = new Expense(positive, DATE, "Coffee", FOOD, CLOCK);

        assertThat(expense.getAmount()).isEqualTo(positive);
    }

    @Test
    void constructor_shouldThrowException_whenDateIsNull() {
        assertThatThrownBy(() -> new Expense(AMOUNT, null, "Coffee", FOOD, CLOCK))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("date must not be null");
    }

    @ParameterizedTest
    @MethodSource("futureDates")
    void constructor_shouldThrowException_whenDateIsInFuture(LocalDate futureDate) {
        assertThatThrownBy(() -> new Expense(AMOUNT, futureDate, "Coffee", FOOD, CLOCK))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("expense date must not be in the future, but was: " + futureDate + " (today: 2026-10-31)");
    }

    @ParameterizedTest
    @MethodSource("pastOrPresentDates")
    void constructor_shouldCreateExpense_whenDateIsNotInFuture(LocalDate date) {
        Expense expense = new Expense(AMOUNT, date, "Coffee", FOOD, CLOCK);

        assertThat(expense.getDate()).isEqualTo(date);
    }

    @ParameterizedTest
    @CsvSource({
        "UTC, 2026-10-31",
        "America/New_York, 2026-10-31",
        "Europe/Kyiv, 2026-11-01",
        "Asia/Tokyo, 2026-11-01"
    })
    void constructor_shouldPlaceDayBoundaryInClockZone(String zone, LocalDate today) {
        Clock lateEvening = Clock.fixed(Instant.parse("2026-10-31T22:30:00Z"), ZoneId.of(zone));

        Expense expense = new Expense(AMOUNT, today, "Coffee", FOOD, lateEvening);

        assertThat(expense.getDate()).isEqualTo(today);
        assertThatThrownBy(() -> new Expense(AMOUNT, today.plusDays(1), "Coffee", FOOD, lateEvening))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void constructor_shouldThrowException_whenClockIsNull() {
        assertThatThrownBy(() -> new Expense(AMOUNT, DATE, "Coffee", FOOD, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("clock must not be null");
    }

    @Test
    void constructor_shouldThrowException_whenCategoryIsNull() {
        assertThatThrownBy(() -> new Expense(AMOUNT, DATE, "Coffee", null, CLOCK))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("category must not be null");
    }

    @Test
    void changeAmount_shouldReplaceAmount_whenAmountIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);
        Money newAmount = Money.of("20.00", "UAH");

        expense.changeAmount(newAmount);

        assertThat(expense.getAmount()).isEqualTo(newAmount);
    }

    @Test
    void changeAmount_shouldThrowException_whenAmountIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);

        assertThatThrownBy(() -> expense.changeAmount(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.004", "-0.01", "-100"})
    void changeAmount_shouldKeepAmount_whenNewAmountIsNotPositive(String amount) {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);
        Money notPositive = Money.of(amount, "UAH");

        assertThatThrownBy(() -> expense.changeAmount(notPositive))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageStartingWith("expense amount must be positive");
        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    void reschedule_shouldChangeDate_whenDateIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);
        LocalDate newDate = LocalDate.of(2026, 10, 15);

        expense.reschedule(newDate, CLOCK);

        assertThat(expense.getDate()).isEqualTo(newDate);
    }

    @Test
    void reschedule_shouldThrowException_whenDateIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);

        assertThatThrownBy(() -> expense.reschedule(null, CLOCK))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("date must not be null");
        assertThat(expense.getDate()).isEqualTo(DATE);
    }

    @ParameterizedTest
    @MethodSource("futureDates")
    void reschedule_shouldKeepDate_whenNewDateIsInFuture(LocalDate futureDate) {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);

        assertThatThrownBy(() -> expense.reschedule(futureDate, CLOCK))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageStartingWith("expense date must not be in the future");
        assertThat(expense.getDate()).isEqualTo(DATE);
    }

    @Test
    void changeDescription_shouldReplaceDescription_whenDescriptionIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);

        expense.changeDescription("Tea");

        assertThat(expense.getDescription()).isEqualTo("Tea");
    }

    @Test
    void changeDescription_shouldClearDescription_whenDescriptionIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);

        expense.changeDescription(null);

        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void moveTo_shouldChangeCategory_whenCategoryIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);
        Category newCategory = new Category("Transport", CategoryType.VARIABLE);

        expense.moveTo(newCategory);

        assertThat(expense.getCategory()).isSameAs(newCategory);
    }

    @Test
    void moveTo_shouldThrowException_whenCategoryIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD, CLOCK);

        assertThatThrownBy(() -> expense.moveTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("category must not be null");
        assertThat(expense.getCategory()).isSameAs(FOOD);
    }

    private static Stream<LocalDate> futureDates() {
        return Stream.of(TODAY.plusDays(1), TODAY.plusMonths(1), TODAY.plusYears(1), LocalDate.MAX);
    }

    private static Stream<LocalDate> pastOrPresentDates() {
        return Stream.of(TODAY, TODAY.minusDays(1), TODAY.minusYears(1), LocalDate.of(2000, 1, 1));
    }
}
