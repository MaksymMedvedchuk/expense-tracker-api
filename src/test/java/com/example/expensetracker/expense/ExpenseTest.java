package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.common.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpenseTest {

    private static final Money AMOUNT = Money.of("12.50", "UAH");
    private static final LocalDate DATE = LocalDate.of(2026, 10, 1);

    @Test
    void constructor_shouldCreateExpense_whenRequiredFieldsAreProvided() {
        Category category = new Category();

        Expense expense = new Expense(AMOUNT, DATE, "Coffee", category);

        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
        assertThat(expense.getDate()).isEqualTo(DATE);
        assertThat(expense.getDescription()).isEqualTo("Coffee");
        assertThat(expense.getCategory()).isSameAs(category);
    }

    @Test
    void constructor_shouldCreateExpense_whenDescriptionIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, null, new Category());

        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void constructor_shouldThrowException_whenAmountIsNull() {
        assertThatThrownBy(() -> new Expense(null, DATE, "Coffee", new Category()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
    }

    @Test
    void constructor_shouldThrowException_whenDateIsNull() {
        assertThatThrownBy(() -> new Expense(AMOUNT, null, "Coffee", new Category()))
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
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());
        Money newAmount = Money.of("20.00", "UAH");

        expense.changeAmount(newAmount);

        assertThat(expense.getAmount()).isEqualTo(newAmount);
    }

    @Test
    void changeAmount_shouldThrowException_whenAmountIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());

        assertThatThrownBy(() -> expense.changeAmount(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    void reschedule_shouldChangeDate_whenDateIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());
        LocalDate newDate = LocalDate.of(2026, 10, 15);

        expense.reschedule(newDate);

        assertThat(expense.getDate()).isEqualTo(newDate);
    }

    @Test
    void reschedule_shouldThrowException_whenDateIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());

        assertThatThrownBy(() -> expense.reschedule(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("date must not be null");
        assertThat(expense.getDate()).isEqualTo(DATE);
    }

    @Test
    void changeDescription_shouldReplaceDescription_whenDescriptionIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());

        expense.changeDescription("Tea");

        assertThat(expense.getDescription()).isEqualTo("Tea");
    }

    @Test
    void changeDescription_shouldClearDescription_whenDescriptionIsNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());

        expense.changeDescription(null);

        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void moveTo_shouldChangeCategory_whenCategoryIsProvided() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());
        Category newCategory = new Category();

        expense.moveTo(newCategory);

        assertThat(expense.getCategory()).isSameAs(newCategory);
    }

    @Test
    void moveTo_shouldThrowException_whenCategoryIsNull() {
        Category category = new Category();
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", category);

        assertThatThrownBy(() -> expense.moveTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("category must not be null");
        assertThat(expense.getCategory()).isSameAs(category);
    }
}
