package com.example.expensetracker.category.web;

import com.example.expensetracker.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType());
    }
}
