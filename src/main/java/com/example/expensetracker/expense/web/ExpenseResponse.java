package com.example.expensetracker.expense.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(
        UUID id, BigDecimal amount, String currency, LocalDate date, String description, UUID categoryId) {
}
