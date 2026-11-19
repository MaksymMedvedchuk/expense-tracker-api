package com.example.expensetracker.expense.web;

import com.example.expensetracker.common.Money;
import com.example.expensetracker.expense.Expense;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class ExpenseMapper {

    public Money toMoney(ExpenseRequest request) {
        return new Money(request.amount(), Currency.getInstance(request.currency()));
    }

    public ExpenseResponse toResponse(Expense expense) {
        Money amount = expense.getAmount();
        return new ExpenseResponse(
                expense.getId(),
                amount.amount(),
                amount.currency().getCurrencyCode(),
                expense.getDate(),
                expense.getDescription(),
                expense.getCategory().getId());
    }
}
