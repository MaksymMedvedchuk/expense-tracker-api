package com.example.expensetracker.common;

/**
 * Base type for violations of business rules, as opposed to programming errors such as null arguments.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
