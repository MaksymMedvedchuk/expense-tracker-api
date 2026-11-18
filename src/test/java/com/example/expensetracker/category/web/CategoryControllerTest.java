package com.example.expensetracker.category.web;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryNotFoundException;
import com.example.expensetracker.category.CategoryService;
import com.example.expensetracker.category.CategoryType;
import com.example.expensetracker.category.DuplicateCategoryNameException;
import com.example.expensetracker.common.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(CategoryMapper.class)
class CategoryControllerTest {

    private static final String FOOD_JSON = """
            {"name": "Food", "type": "VARIABLE"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void create_shouldReturn201WithLocation_whenCategoryIsCreated() throws Exception {
        Category food = new Category("Food", CategoryType.VARIABLE);
        when(categoryService.create("Food", CategoryType.VARIABLE)).thenReturn(food);

        mockMvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON).content(FOOD_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/categories/" + food.getId()))
                .andExpect(jsonPath("$.id").value(food.getId().toString()))
                .andExpect(jsonPath("$.name").value("Food"))
                .andExpect(jsonPath("$.type").value("VARIABLE"));
    }

    @Test
    void create_shouldReturn409_whenNameAlreadyExists() throws Exception {
        when(categoryService.create("Food", CategoryType.VARIABLE))
                .thenThrow(new DuplicateCategoryNameException("Food"));

        mockMvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON).content(FOOD_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("category with name 'Food' already exists"));
    }

    @Test
    void create_shouldReturn400_whenNameIsBlank() throws Exception {
        when(categoryService.create("  ", CategoryType.VARIABLE))
                .thenThrow(new BusinessRuleViolationException("category name must not be blank"));

        mockMvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "  ", "type": "VARIABLE"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("category name must not be blank"));
    }

    @Test
    void findAll_shouldReturnAllCategories() throws Exception {
        Category food = new Category("Food", CategoryType.VARIABLE);
        Category rent = new Category("Rent", CategoryType.FIXED);
        when(categoryService.findAll()).thenReturn(List.of(food, rent));

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Food"))
                .andExpect(jsonPath("$[1].name").value("Rent"));
    }

    @Test
    void findById_shouldReturnCategory_whenItExists() throws Exception {
        Category food = new Category("Food", CategoryType.VARIABLE);
        when(categoryService.findById(food.getId())).thenReturn(food);

        mockMvc.perform(get("/api/v1/categories/{id}", food.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(food.getId().toString()))
                .andExpect(jsonPath("$.name").value("Food"));
    }

    @Test
    void findById_shouldReturn404_whenCategoryDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        when(categoryService.findById(id)).thenThrow(new CategoryNotFoundException(id));

        mockMvc.perform(get("/api/v1/categories/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("category with id '" + id + "' not found"));
    }

    @Test
    void findById_shouldReturn400_whenIdIsNotUuid() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{id}", "not-a-uuid"))
                .andExpect(status().isBadRequest());
    }
}
