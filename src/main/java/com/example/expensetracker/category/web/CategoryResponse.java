package com.example.expensetracker.category.web;

import com.example.expensetracker.category.CategoryType;

import java.util.UUID;

public record CategoryResponse(UUID id, String name, CategoryType type) {
}
