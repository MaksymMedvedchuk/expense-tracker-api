package com.example.expensetracker.category.web;

import com.example.expensetracker.category.CategoryType;

public record CategoryRequest(String name, CategoryType type) {
}
