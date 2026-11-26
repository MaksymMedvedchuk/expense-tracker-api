package com.example.expensetracker.expense;

import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryExpenseRepository implements ExpenseRepository {

    /**
     * Id breaks ties between expenses on the same date, so pages stay stable between requests.
     */
    private static final Comparator<Expense> NEWEST_FIRST = Comparator.comparing(Expense::getDate).reversed()
            .thenComparing(Expense::getId);

    private final Map<UUID, Expense> expenses = new ConcurrentHashMap<>();

    @Override
    public List<Expense> findBetween(LocalDate from, LocalDate to) {
        return findMatching(new ExpenseFilter(from, to, null));
    }

    @Override
    public Expense save(Expense expense) {
        expenses.put(expense.getId(), expense);
        return expense;
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return Optional.ofNullable(expenses.get(id));
    }

    @Override
    public Page<Expense> findAll(ExpenseFilter filter, PageRequest pageRequest) {
        List<Expense> matching = findMatching(filter);
        List<Expense> content = matching.stream()
                .skip(pageRequest.offset())
                .limit(pageRequest.size())
                .toList();
        return new Page<>(content, pageRequest, matching.size());
    }

    @Override
    public void deleteById(UUID id) {
        expenses.remove(id);
    }

    @Override
    public boolean existsByCategoryId(UUID categoryId) {
        return expenses.values().stream()
                .anyMatch(expense -> expense.getCategory().getId().equals(categoryId));
    }

    private List<Expense> findMatching(ExpenseFilter filter) {
        return expenses.values().stream()
                .filter(filter::matches)
                .sorted(NEWEST_FIRST)
                .toList();
    }
}
