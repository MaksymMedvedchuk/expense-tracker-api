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
}
