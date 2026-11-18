package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.Money;
import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryExpenseRepositoryTest {

    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);
    private static final Category RENT = new Category("Rent", CategoryType.FIXED);
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2027-01-01T00:00:00Z"), ZoneOffset.UTC);
    private static final ExpenseFilter NO_FILTER = new ExpenseFilter(null, null, null);

    private static final LocalDate FROM = LocalDate.of(2026, 11, 1);
    private static final LocalDate TO = LocalDate.of(2026, 11, 30);

    private final InMemoryExpenseRepository repository = new InMemoryExpenseRepository();

    @Test
    void findBetween_shouldReturnEmptyList_whenThereAreNoExpenses() {
        List<Expense> found = repository.findBetween(FROM, TO);

        assertThat(found).isEmpty();
    }

    @Test
    void findBetween_shouldReturnOnlyExpensesWithinPeriodIncludingBoundaries() {
        repository.save(expenseOn(FROM.minusDays(1), FOOD));
        Expense firstDay = repository.save(expenseOn(FROM, FOOD));
        Expense middle = repository.save(expenseOn(LocalDate.of(2026, 11, 15), FOOD));
        Expense lastDay = repository.save(expenseOn(TO, FOOD));
        repository.save(expenseOn(TO.plusDays(1), FOOD));

        List<Expense> found = repository.findBetween(FROM, TO);

        assertThat(found).containsExactlyInAnyOrder(firstDay, middle, lastDay);
    }

    @Test
    void findById_shouldReturnSavedExpense() {
        Expense expense = repository.save(expenseOn(FROM, FOOD));

        assertThat(repository.findById(expense.getId())).containsSame(expense);
    }

    @Test
    void findById_shouldReturnEmpty_whenExpenseIsMissing() {
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findAll_shouldReturnMatchingExpensesNewestFirst() {
        Expense older = repository.save(expenseOn(FROM, FOOD));
        Expense newer = repository.save(expenseOn(TO, FOOD));
        repository.save(expenseOn(TO, RENT));

        Page<Expense> page = repository.findAll(new ExpenseFilter(null, null, FOOD.getId()), new PageRequest(0, 10));

        assertThat(page.content()).containsExactly(newer, older);
        assertThat(page.totalElements()).isEqualTo(2);
    }

    @Test
    void findAll_shouldReturnRequestedPageAndTotalOfAllMatches() {
        Expense third = repository.save(expenseOn(FROM.plusDays(2), FOOD));
        Expense fourth = repository.save(expenseOn(FROM.plusDays(1), FOOD));
        repository.save(expenseOn(FROM, FOOD));
        repository.save(expenseOn(FROM.plusDays(3), FOOD));
        repository.save(expenseOn(FROM.plusDays(4), FOOD));

        Page<Expense> page = repository.findAll(NO_FILTER, new PageRequest(1, 2));

        assertThat(page.content()).containsExactly(third, fourth);
        assertThat(page.totalElements()).isEqualTo(5);
    }

    @Test
    void deleteById_shouldRemoveExpense() {
        Expense expense = repository.save(expenseOn(FROM, FOOD));

        repository.deleteById(expense.getId());

        assertThat(repository.findById(expense.getId())).isEmpty();
    }

    private static Expense expenseOn(LocalDate date, Category category) {
        return new Expense(Money.of("100.00", "UAH"), date, "Expense", category, CLOCK);
    }
}
