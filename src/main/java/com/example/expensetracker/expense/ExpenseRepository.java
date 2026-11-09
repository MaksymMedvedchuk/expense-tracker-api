package com.example.expensetracker.expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository {

    /**
     * Returns expenses dated within the period, both boundaries included.
     */
    List<Expense> findBetween(LocalDate from, LocalDate to);
}
