package com.example.expensetracker.expense.web;

import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;
import com.example.expensetracker.common.web.PageResponse;
import com.example.expensetracker.expense.Expense;
import com.example.expensetracker.expense.ExpenseFilter;
import com.example.expensetracker.expense.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private static final String DEFAULT_PAGE_SIZE = "5";
    private static final int MAX_PAGE_SIZE = 100;

    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    public ExpenseController(ExpenseService expenseService, ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@RequestBody ExpenseRequest request) {
        Expense created = expenseService.create(
                expenseMapper.toMoney(request), request.date(), request.description(), request.categoryId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(expenseMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ExpenseResponse findById(@PathVariable UUID id) {
        return expenseMapper.toResponse(expenseService.findById(id));
    }

    @GetMapping
    public PageResponse<ExpenseResponse> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        if (size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("size must not exceed " + MAX_PAGE_SIZE + ", but was: " + size);
        }
        Page<Expense> expenses = expenseService.findAll(
                new ExpenseFilter(from, to, categoryId), new PageRequest(page, size));
        return PageResponse.from(expenses, expenseMapper::toResponse);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(@PathVariable UUID id, @RequestBody ExpenseRequest request) {
        Expense updated = expenseService.update(
                id, expenseMapper.toMoney(request), request.date(), request.description(), request.categoryId());
        return expenseMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        expenseService.delete(id);
    }
}
