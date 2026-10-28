package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.common.Money;

import java.time.LocalDate;
import java.util.Objects;

public class Expense {

    private Money amount;
    private LocalDate date;
    private String description;
    private Category category;

    public Expense(Money amount, LocalDate date, String description, Category category) {
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.date = Objects.requireNonNull(date, "date must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.description = description;
    }

    public Money getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public void changeAmount(Money newAmount) {
        this.amount = Objects.requireNonNull(newAmount, "amount must not be null");
    }

    public void reschedule(LocalDate newDate) {
        this.date = Objects.requireNonNull(newDate, "date must not be null");
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void moveTo(Category newCategory) {
        this.category = Objects.requireNonNull(newCategory, "category must not be null");
    }
}
