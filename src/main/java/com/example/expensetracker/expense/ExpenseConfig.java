package com.example.expensetracker.expense;

import com.example.expensetracker.category.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Registers domain services as beans, so the domain classes themselves stay free of Spring.
 */
@Configuration
class ExpenseConfig {

    @Bean
    ExpenseService expenseService(ExpenseRepository expenseRepository, CategoryService categoryService, Clock clock) {
        return new ExpenseService(expenseRepository, categoryService, clock);
    }
}
