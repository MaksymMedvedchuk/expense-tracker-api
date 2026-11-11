package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.BusinessRuleViolationException;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpenseTest {

    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);
    private static final Money AMOUNT = Money.of("12.50", "UAH");
    private static final LocalDate DATE = LocalDate.of(2026, 10, 1);

    @Test
    void constructor_shouldCreateExpense_whenRequiredFieldsAreProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);

        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
        assertThat(expense.getDate()).isEqualTo(DATE);
        assertThat(expense.getDescription()).isEqualTo("Coffee");
        assertThat(expense.getCategory()).isSameAs(FOOD);
    }

    @Test
    void constructor_shouldCreateExpense_whenDescriptionIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, null, FOOD);

        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void constructor_shouldThrowException_whenAmountIsNull() {
        assertThatThrownBy(() -> new Expense(null, DATE, "Coffee", FOOD))
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

        assertThatThrownBy(() -> new Expense(notPositive, DATE, "Coffee", FOOD))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("expense amount must be positive, but was: " + roundedAmount + " UAH");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.01", "0.006", "1.00", "1000000.00"})
    void constructor_shouldCreateExpense_whenAmountIsPositive(String amount) {
        Money positive = Money.of(amount, "UAH");

        Expense expense = new Expense(positive, DATE, "Coffee", FOOD);

        assertThat(expense.getAmount()).isEqualTo(positive);
    }

    @Test
    void constructor_shouldThrowException_whenDateIsNull() {
        assertThatThrownBy(() -> new Expense(AMOUNT, null, "Coffee", FOOD))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("date must not be null");
    }

    @Test
    void constructor_shouldThrowException_whenCategoryIsNull() {
        assertThatThrownBy(() -> new Expense(AMOUNT, DATE, "Coffee", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("category must not be null");
    }

    @Test
    void changeAmount_shouldReplaceAmount_whenAmountIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);
        Money newAmount = Money.of("20.00", "UAH");

        expense.changeAmount(newAmount);

        assertThat(expense.getAmount()).isEqualTo(newAmount);
    }

    @Test
    void changeAmount_shouldThrowException_whenAmountIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);

        assertThatThrownBy(() -> expense.changeAmount(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.004", "-0.01", "-100"})
    void changeAmount_shouldKeepAmount_whenNewAmountIsNotPositive(String amount) {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);
        Money notPositive = Money.of(amount, "UAH");

        assertThatThrownBy(() -> expense.changeAmount(notPositive))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageStartingWith("expense amount must be positive");
        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    void reschedule_shouldChangeDate_whenDateIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);
        LocalDate newDate = LocalDate.of(2026, 10, 15);

        expense.reschedule(newDate);

        assertThat(expense.getDate()).isEqualTo(newDate);
    }

    @Test
    void reschedule_shouldThrowException_whenDateIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);

        assertThatThrownBy(() -> expense.reschedule(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("date must not be null");
        assertThat(expense.getDate()).isEqualTo(DATE);
    }

    @Test
    void changeDescription_shouldReplaceDescription_whenDescriptionIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);

        expense.changeDescription("Tea");

        assertThat(expense.getDescription()).isEqualTo("Tea");
    }

    @Test
    void changeDescription_shouldClearDescription_whenDescriptionIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);

        expense.changeDescription(null);

        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void moveTo_shouldChangeCategory_whenCategoryIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);
        Category newCategory = new Category("Transport", CategoryType.VARIABLE);

        expense.moveTo(newCategory);

        assertThat(expense.getCategory()).isSameAs(newCategory);
    }

    @Test
    void moveTo_shouldThrowException_whenCategoryIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", FOOD);

        assertThatThrownBy(() -> expense.moveTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("category must not be null");
        assertThat(expense.getCategory()).isSameAs(FOOD);
    }
}
