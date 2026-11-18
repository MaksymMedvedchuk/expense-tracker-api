package com.example.expensetracker.category.web;

import com.example.expensetracker.category.CategoryNotFoundException;
import com.example.expensetracker.category.DuplicateCategoryNameException;
import com.example.expensetracker.common.BusinessRuleViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps category domain exceptions to HTTP statuses. Scoped to {@link CategoryController}
 * until a global handler is introduced.
 */
@RestControllerAdvice(assignableTypes = CategoryController.class)
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ProblemDetail handleNotFound(CategoryNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DuplicateCategoryNameException.class)
    public ProblemDetail handleDuplicateName(DuplicateCategoryNameException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ProblemDetail handleBusinessRuleViolation(BusinessRuleViolationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
