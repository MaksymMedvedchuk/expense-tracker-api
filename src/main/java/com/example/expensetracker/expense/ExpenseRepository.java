package com.example.expensetracker.expense;

import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {

    /**
     * Returns expenses dated within the period, both boundaries included.
     */
    List<Expense> findBetween(LocalDate from, LocalDate to);

    Expense save(Expense expense);

    Optional<Expense> findById(UUID id);

    /**
     * Returns one page of expenses matching the filter, newest first.
     */
    Page<Expense> findAll(ExpenseFilter filter, PageRequest pageRequest);

    void deleteById(UUID id);

    boolean existsByCategoryId(UUID categoryId);
}
