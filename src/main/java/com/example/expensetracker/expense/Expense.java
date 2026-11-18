package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.common.BusinessRuleViolationException;
import com.example.expensetracker.common.Money;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Expense {

    private final UUID id;
    private Money amount;
    private LocalDate date;
    private String description;
    private Category category;

    public Expense(Money amount, LocalDate date, String description, Category category, Clock clock) {
        this.id = UUID.randomUUID();
        this.amount = requirePositive(amount);
        this.date = requireNotInFuture(date, clock);
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.description = description;
    }

    public UUID getId() {
        return id;
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
        this.amount = requirePositive(newAmount);
    }

    public void reschedule(LocalDate newDate, Clock clock) {
        this.date = requireNotInFuture(newDate, clock);
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void moveTo(Category newCategory) {
        this.category = Objects.requireNonNull(newCategory, "category must not be null");
    }

    /**
     * Replaces all editable fields together; every value is validated before any field changes,
     * so a failed update leaves the expense untouched.
     */
    public void update(Money newAmount, LocalDate newDate, String newDescription, Category newCategory, Clock clock) {
        Money validAmount = requirePositive(newAmount);
        LocalDate validDate = requireNotInFuture(newDate, clock);
        Category validCategory = Objects.requireNonNull(newCategory, "category must not be null");
        this.amount = validAmount;
        this.date = validDate;
        this.description = newDescription;
        this.category = validCategory;
    }

    private static Money requirePositive(Money amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (!amount.isPositive()) {
            throw new BusinessRuleViolationException(
                    "expense amount must be positive, but was: " + amount.amount() + " " + amount.currency());
        }
        return amount;
    }

    private static LocalDate requireNotInFuture(LocalDate date, Clock clock) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(clock, "clock must not be null");
        LocalDate today = LocalDate.now(clock);
        if (date.isAfter(today)) {
            throw new BusinessRuleViolationException(
                    "expense date must not be in the future, but was: " + date + " (today: " + today + ")");
        }
        return date;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Expense other)) {
            return false;
        }
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
