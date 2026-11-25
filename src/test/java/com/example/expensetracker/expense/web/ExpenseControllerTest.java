package com.example.expensetracker.expense.web;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryNotFoundException;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.common.BusinessRuleViolationException;
import com.example.expensetracker.common.Money;
import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;
import com.example.expensetracker.expense.Expense;
import com.example.expensetracker.expense.ExpenseFilter;
import com.example.expensetracker.expense.ExpenseNotFoundException;
import com.example.expensetracker.expense.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
@Import(ExpenseMapper.class)
class ExpenseControllerTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-11-20T10:00:00Z"), ZoneOffset.UTC);
    private static final LocalDate DATE = LocalDate.of(2026, 11, 15);
    private static final Category FOOD = new Category("Food", CategoryType.VARIABLE);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @Test
    void create_shouldReturn201WithLocation_whenExpenseIsCreated() throws Exception {
        Expense lunch = new Expense(Money.of("150.50", "UAH"), DATE, "Lunch", FOOD, CLOCK);
        when(expenseService.create(Money.of("150.50", "UAH"), DATE, "Lunch", FOOD.getId())).thenReturn(lunch);

        mockMvc.perform(post("/api/v1/expenses").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson("150.50", "Lunch", FOOD.getId())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/expenses/" + lunch.getId()))
                .andExpect(jsonPath("$.id").value(lunch.getId().toString()))
                .andExpect(jsonPath("$.amount").value(150.50))
                .andExpect(jsonPath("$.currency").value("UAH"))
                .andExpect(jsonPath("$.date").value("2026-11-15"))
                .andExpect(jsonPath("$.description").value("Lunch"))
                .andExpect(jsonPath("$.categoryId").value(FOOD.getId().toString()));
    }

    @Test
    void create_shouldReturn400_whenCategoryDoesNotExist() throws Exception {
        UUID missingCategoryId = UUID.randomUUID();
        when(expenseService.create(Money.of("150.50", "UAH"), DATE, "Lunch", missingCategoryId))
                .thenThrow(new CategoryNotFoundException(missingCategoryId));

        mockMvc.perform(post("/api/v1/expenses").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson("150.50", "Lunch", missingCategoryId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("category with id '" + missingCategoryId + "' not found"));
    }

    @Test
    void create_shouldReturn400_whenAmountIsNotPositive() throws Exception {
        when(expenseService.create(Money.of("0", "UAH"), DATE, "Lunch", FOOD.getId()))
                .thenThrow(new BusinessRuleViolationException("expense amount must be positive, but was: 0.00 UAH"));

        mockMvc.perform(post("/api/v1/expenses").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson("0", "Lunch", FOOD.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("expense amount must be positive, but was: 0.00 UAH"));
    }

    @Test
    void findById_shouldReturnExpense_whenItExists() throws Exception {
        Expense lunch = new Expense(Money.of("150.50", "UAH"), DATE, "Lunch", FOOD, CLOCK);
        when(expenseService.findById(lunch.getId())).thenReturn(lunch);

        mockMvc.perform(get("/api/v1/expenses/{id}", lunch.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(lunch.getId().toString()))
                .andExpect(jsonPath("$.description").value("Lunch"));
    }

    @Test
    void findById_shouldReturn404_whenExpenseDoesNotExist() throws Exception {
        UUID missingId = UUID.randomUUID();
        when(expenseService.findById(missingId)).thenThrow(new ExpenseNotFoundException(missingId));

        mockMvc.perform(get("/api/v1/expenses/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("expense with id '" + missingId + "' not found"));
    }

    @Test
    void findAll_shouldReturnPageOfExpenses_whenFiltersAndPageAreGiven() throws Exception {
        Expense lunch = new Expense(Money.of("150.50", "UAH"), DATE, "Lunch", FOOD, CLOCK);
        ExpenseFilter filter = new ExpenseFilter(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), FOOD.getId());
        PageRequest pageRequest = new PageRequest(1, 2);
        when(expenseService.findAll(filter, pageRequest)).thenReturn(new Page<>(List.of(lunch), pageRequest, 3));

        mockMvc.perform(get("/api/v1/expenses")
                        .param("from", "2026-11-01")
                        .param("to", "2026-11-30")
                        .param("categoryId", FOOD.getId().toString())
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(lunch.getId().toString()))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void findAll_shouldUseFirstPageOfFiveWithoutFilters_whenNoParamsAreGiven() throws Exception {
        PageRequest defaultPageRequest = new PageRequest(0, 5);
        when(expenseService.findAll(new ExpenseFilter(null, null, null), defaultPageRequest))
                .thenReturn(new Page<>(List.of(), defaultPageRequest, 0));

        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(5));
    }

    @Test
    void findAll_shouldReject_whenSizeExceedsMaximum() {
        assertThatThrownBy(() -> mockMvc.perform(get("/api/v1/expenses").param("size", "101")))
                .hasRootCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("size must not exceed 100, but was: 101");
    }

    private static String requestJson(String amount, String description, UUID categoryId) {
        return """
                {"amount": %s, "currency": "UAH", "date": "2026-11-15", "description": "%s", "categoryId": "%s"}
                """.formatted(amount, description, categoryId);
    }
}
