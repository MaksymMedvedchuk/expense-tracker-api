package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryService;
import com.example.expensetracker.common.Money;
import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final Clock clock;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryService categoryService, Clock clock) {
        this.expenseRepository = Objects.requireNonNull(expenseRepository, "expenseRepository must not be null");
        this.categoryService = Objects.requireNonNull(categoryService, "categoryService must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public Expense create(Money amount, LocalDate date, String description, UUID categoryId) {
        Category category = categoryService.findById(categoryId);
        Expense expense = new Expense(amount, date, description, category, clock);
        return expenseRepository.save(expense);
    }

    public Expense findById(UUID id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    public Page<Expense> findAll(ExpenseFilter filter, PageRequest pageRequest) {
        return expenseRepository.findAll(filter, pageRequest);
    }
}
