package com.example.expensetracker.category;

import com.example.expensetracker.common.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private CategoryService service;

    @BeforeEach
    void setUp() {
        service = new CategoryService(categoryRepository);
    }

    @Test
    void constructor_shouldThrowException_whenRepositoryIsNull() {
        assertThatThrownBy(() -> new CategoryService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("categoryRepository must not be null");
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
}
