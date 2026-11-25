package com.example.expensetracker.expense.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseRequest(BigDecimal amount, String currency, LocalDate date, String description, UUID categoryId) {
}
