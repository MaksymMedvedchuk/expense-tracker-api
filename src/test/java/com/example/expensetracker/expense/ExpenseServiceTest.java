package com.example.expensetracker.expense;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryNotFoundException;
import com.example.expensetracker.category.CategoryService;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.BusinessRuleViolationException;
import com.example.expensetracker.common.Money;
import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-11-18T10:00:00Z"), ZoneOffset.UTC);
    private static final LocalDate TODAY = LocalDate.of(2026, 11, 18);
    private static final Money AMOUNT = Money.of("100.00", "UAH");
    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryService categoryService;

    private ExpenseService service;

    @BeforeEach
    void setUp() {
        service = new ExpenseService(expenseRepository, categoryService, CLOCK);
    }

    @Test
    void constructor_shouldThrowException_whenClockIsNull() {
        assertThatThrownBy(() -> new ExpenseService(expenseRepository, categoryService, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("clock must not be null");
    }

    @Test
    void create_shouldSaveExpenseWithCategoryFoundById() {
        when(categoryService.findById(FOOD.getId())).thenReturn(FOOD);
        when(expenseRepository.save(any())).then(returnsFirstArg());

        Expense created = service.create(AMOUNT, TODAY, "Lunch", FOOD.getId());

        assertThat(created.getCategory()).isSameAs(FOOD);
        assertThat(created.getAmount()).isEqualTo(AMOUNT);
        assertThat(created.getDate()).isEqualTo(TODAY);
        assertThat(created.getDescription()).isEqualTo("Lunch");
    }

    @Test
    void create_shouldNotSave_whenCategoryDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        when(categoryService.findById(missingId)).thenThrow(new CategoryNotFoundException(missingId));

        assertThatThrownBy(() -> service.create(AMOUNT, TODAY, "Lunch", missingId))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void create_shouldRejectDateAfterTodayOfInjectedClock() {
        when(categoryService.findById(FOOD.getId())).thenReturn(FOOD);

        assertThatThrownBy(() -> service.create(AMOUNT, TODAY.plusDays(1), "Lunch", FOOD.getId()))
                .isInstanceOf(BusinessRuleViolationException.class);
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnExpense_whenItExists() {
        Expense expense = new Expense(AMOUNT, TODAY, "Lunch", FOOD, CLOCK);
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));

        assertThat(service.findById(expense.getId())).isSameAs(expense);
    }

    @Test
    void findById_shouldThrowException_whenExpenseDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        when(expenseRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(missingId))
                .isInstanceOf(ExpenseNotFoundException.class)
                .hasMessage("expense with id '" + missingId + "' not found");
    }

    @Test
    void findAll_shouldReturnPageFromRepository() {
        ExpenseFilter filter = new ExpenseFilter(null, null, FOOD.getId());
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<Expense> page = new Page<>(List.of(new Expense(AMOUNT, TODAY, "Lunch", FOOD, CLOCK)), pageRequest, 1);
        when(expenseRepository.findAll(filter, pageRequest)).thenReturn(page);

        assertThat(service.findAll(filter, pageRequest)).isSameAs(page);
    }

    @Test
    void update_shouldReplaceFieldsAndMoveExpenseToCategoryFoundById() {
        Expense expense = new Expense(AMOUNT, TODAY, "Lunch", FOOD, CLOCK);
        Category rent = new Category("Rent", CategoryType.FIXED);
        Money newAmount = Money.of("5000.00", "UAH");
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        when(categoryService.findById(rent.getId())).thenReturn(rent);
        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense updated = service.update(expense.getId(), newAmount, TODAY.minusDays(1), "Flat", rent.getId());

        assertThat(updated.getAmount()).isEqualTo(newAmount);
        assertThat(updated.getDate()).isEqualTo(TODAY.minusDays(1));
        assertThat(updated.getDescription()).isEqualTo("Flat");
        assertThat(updated.getCategory()).isSameAs(rent);
    }

    @Test
    void update_shouldNotSave_whenExpenseDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        when(expenseRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(missingId, AMOUNT, TODAY, "Lunch", FOOD.getId()))
                .isInstanceOf(ExpenseNotFoundException.class);
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void update_shouldKeepExpenseUnchanged_whenCategoryDoesNotExist() {
        Expense expense = new Expense(AMOUNT, TODAY, "Lunch", FOOD, CLOCK);
        UUID missingCategoryId = UUID.randomUUID();
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        when(categoryService.findById(missingCategoryId)).thenThrow(new CategoryNotFoundException(missingCategoryId));

        assertThatThrownBy(() -> service.update(expense.getId(), Money.of("1.00", "UAH"), TODAY, "Flat",
                missingCategoryId))
                .isInstanceOf(CategoryNotFoundException.class);
        assertThat(expense.getAmount()).isEqualTo(AMOUNT);
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void delete_shouldRemoveExpense_whenItExists() {
        Expense expense = new Expense(AMOUNT, TODAY, "Lunch", FOOD, CLOCK);
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));

        service.delete(expense.getId());

        verify(expenseRepository).deleteById(expense.getId());
    }

    @Test
    void delete_shouldThrowException_whenExpenseDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        when(expenseRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(missingId))
                .isInstanceOf(ExpenseNotFoundException.class);
        verify(expenseRepository, never()).deleteById(any());
    }
}
