package com.example.expensetracker.expense.web;

import com.example.expensetracker.category.CategoryNotFoundException;
import com.example.expensetracker.common.BusinessRuleViolationException;
import com.example.expensetracker.expense.ExpenseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps expense domain exceptions to HTTP statuses. Scoped to {@link ExpenseController}
 * until a global handler is introduced.
 */
@RestControllerAdvice(assignableTypes = ExpenseController.class)
public class ExpenseExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ProblemDetail handleNotFound(ExpenseNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * The request URL exists; it is the category referenced in the body that is missing, so this is a bad request.
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ProblemDetail handleMissingCategory(CategoryNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ProblemDetail handleBusinessRuleViolation(BusinessRuleViolationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
