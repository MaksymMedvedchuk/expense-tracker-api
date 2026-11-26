package com.example.expensetracker.category;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers domain services as beans, so the domain classes themselves stay free of Spring.
 */
@Configuration
class CategoryConfig {

    @Bean
    CategoryService categoryService(CategoryRepository categoryRepository, CategoryUsage categoryUsage) {
        return new CategoryService(categoryRepository, categoryUsage);
    }
}
