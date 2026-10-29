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
    void constructorShouldCreateExpenseWithRequiredFields() {
        Category category = new Category();

        Expense expense = new Expense(AMOUNT, DATE, "Coffee", category);

        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
        assertThat(expense.getDate()).isEqualTo(DATE);
        assertThat(expense.getDescription()).isEqualTo("Coffee");
        assertThat(expense.getCategory()).isSameAs(category);
    }

    @Test
    void constructorShouldAllowMissingDescription() {
        Expense expense = new Expense(AMOUNT, DATE, null, new Category());

        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void constructorShouldRejectNullAmount() {
        assertThatThrownBy(() -> new Expense(null, DATE, "Coffee", new Category()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
    }

    @Test
    void constructorShouldRejectNullDate() {
        assertThatThrownBy(() -> new Expense(AMOUNT, null, "Coffee", new Category()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("date must not be null");
    }

    @Test
    void constructorShouldRejectNullCategory() {
        assertThatThrownBy(() -> new Expense(AMOUNT, DATE, "Coffee", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("category must not be null");
    }

    @Test
    void changeAmountShouldRejectNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());

        assertThatThrownBy(() -> expense.changeAmount(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    void rescheduleShouldRejectNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());

        assertThatThrownBy(() -> expense.reschedule(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("date must not be null");
    }

    @Test
    void moveToShouldChangeCategory() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());
        Category newCategory = new Category();

        expense.moveTo(newCategory);

        assertThat(expense.getCategory()).isSameAs(newCategory);
    }

    @Test
    void moveToShouldRejectNull() {
        Expense expense = new Expense(AMOUNT, DATE, "Coffee", new Category());

        assertThatThrownBy(() -> expense.moveTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("category must not be null");
    }
}
