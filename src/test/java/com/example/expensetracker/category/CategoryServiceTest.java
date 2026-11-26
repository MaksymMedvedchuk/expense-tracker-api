package com.example.expensetracker.category;

import com.example.expensetracker.common.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryUsage categoryUsage;

    private CategoryService service;

    @BeforeEach
    void setUp() {
        service = new CategoryService(categoryRepository, categoryUsage);
    }

    @Test
    void constructor_shouldThrowException_whenRepositoryIsNull() {
        assertThatThrownBy(() -> new CategoryService(null, categoryUsage))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("categoryRepository must not be null");
    }

    @Test
    void constructor_shouldThrowException_whenCategoryUsageIsNull() {
        assertThatThrownBy(() -> new CategoryService(categoryRepository, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("categoryUsage must not be null");
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "Food|Food",
        "' Food '|Food",
        "'\tFood\n'|Food",
        "'  fOoD'|fOoD"
    })
    void create_shouldThrowException_whenNameAlreadyExists(String name, String strippedName) {
        when(categoryRepository.existsByNameIgnoreCase(strippedName)).thenReturn(true);

        assertThatThrownBy(() -> service.create(name, CategoryType.VARIABLE))
                .isInstanceOf(DuplicateCategoryNameException.class)
                .hasMessage("category with name '" + strippedName + "' already exists");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void create_shouldSaveCategory_whenNameIsUnique() {
        when(categoryRepository.existsByNameIgnoreCase("Food")).thenReturn(false);
        when(categoryRepository.save(any())).thenAnswer(returnsFirstArg());

        Category created = service.create(" Food ", CategoryType.FIXED);

        assertThat(created.getName()).isEqualTo("Food");
        assertThat(created.getType()).isEqualTo(CategoryType.FIXED);
        verify(categoryRepository).save(created);
    }

    @Test
    void create_shouldNotSaveCategory_whenNameIsBlank() {
        assertThatThrownBy(() -> service.create("  ", CategoryType.VARIABLE))
                .isInstanceOf(BusinessRuleViolationException.class);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnCategory_whenItExists() {
        Category food = new Category("Food", CategoryType.VARIABLE);
        when(categoryRepository.findById(food.getId())).thenReturn(Optional.of(food));

        assertThat(service.findById(food.getId())).isSameAs(food);
    }

    @Test
    void findById_shouldThrowException_whenCategoryDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("category with id '" + id + "' not found");
    }

    @Test
    void findAll_shouldReturnCategoriesFromRepository() {
        List<Category> categories = List.of(new Category("Food", CategoryType.VARIABLE));
        when(categoryRepository.findAll()).thenReturn(categories);

        assertThat(service.findAll()).isEqualTo(categories);
    }

    @Test
    void update_shouldSaveCategoryWithNewNameAndType() {
        Category category = new Category("Food", CategoryType.VARIABLE);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCaseAndIdNot("Rent", category.getId())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category updated = service.update(category.getId(), " Rent ", CategoryType.FIXED);

        assertThat(updated.getName()).isEqualTo("Rent");
        assertThat(updated.getType()).isEqualTo(CategoryType.FIXED);
    }

    @Test
    void update_shouldThrowExceptionAndKeepCategoryUnchanged_whenNameBelongsToAnotherCategory() {
        Category category = new Category("Food", CategoryType.VARIABLE);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCaseAndIdNot("Rent", category.getId())).thenReturn(true);

        assertThatThrownBy(() -> service.update(category.getId(), "Rent", CategoryType.FIXED))
                .isInstanceOf(DuplicateCategoryNameException.class);
        assertThat(category.getName()).isEqualTo("Food");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowException_whenCategoryDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, "Rent", CategoryType.FIXED))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteCategory_whenItExists() {
        Category category = new Category("Food", CategoryType.VARIABLE);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        service.delete(category.getId());

        verify(categoryRepository).deleteById(category.getId());
    }

    @Test
    void delete_shouldThrowException_whenCategoryDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void delete_shouldThrowException_whenCategoryIsInUse() {
        Category category = new Category("Food", CategoryType.VARIABLE);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryUsage.isInUse(category.getId())).thenReturn(true);

        assertThatThrownBy(() -> service.delete(category.getId()))
                .isInstanceOf(CategoryInUseException.class)
                .hasMessage("category with id '" + category.getId() + "' is used by expenses");
        verify(categoryRepository, never()).deleteById(any());
    }
}
