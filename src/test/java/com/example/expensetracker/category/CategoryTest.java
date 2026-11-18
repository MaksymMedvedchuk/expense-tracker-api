package com.example.expensetracker.category;

import com.example.expensetracker.common.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void constructor_shouldThrowException_whenNameIsBlank(String name) {
        assertThatThrownBy(() -> new Category(name, CategoryType.VARIABLE))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageStartingWith("category name must not be blank");
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "Food|Food",
        "FOOD|FOOD",
        "' Food '|Food",
        "'\tFood\n'|Food",
        "F|F",
        "'Eating out'|'Eating out'"
    })
    void constructor_shouldStripName_whenNameIsNotBlank(String name, String expected) {
        Category category = new Category(name, CategoryType.VARIABLE);

        assertThat(category.getName()).isEqualTo(expected);
    }

    @Test
    void constructor_shouldThrowException_whenNameIsNull() {
        assertThatThrownBy(() -> new Category(null, CategoryType.VARIABLE))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void constructor_shouldThrowException_whenTypeIsNull() {
        assertThatThrownBy(() -> new Category("Food", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("type must not be null");
    }

    @Test
    void constructor_shouldGenerateUniqueId() {
        Category first = new Category("Food", CategoryType.VARIABLE);
        Category second = new Category("Food", CategoryType.VARIABLE);

        assertThat(first.getId()).isNotNull().isNotEqualTo(second.getId());
    }

    @Test
    void equals_shouldReturnFalse_whenIdsDifferEvenIfNamesAreEqual() {
        Category first = new Category("Food", CategoryType.VARIABLE);
        Category second = new Category("Food", CategoryType.VARIABLE);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void hashCode_shouldStayStable_whenCategoryIsUpdated() {
        Category category = new Category("Food", CategoryType.VARIABLE);
        Set<Category> categories = new HashSet<>(Set.of(category));

        category.update("Groceries", CategoryType.FIXED);

        assertThat(categories.contains(category)).isTrue();
    }

    @Test
    void update_shouldReplaceNameAndType() {
        Category category = new Category("Food", CategoryType.VARIABLE);

        category.update(" Rent ", CategoryType.FIXED);

        assertThat(category.getName()).isEqualTo("Rent");
        assertThat(category.getType()).isEqualTo(CategoryType.FIXED);
    }

    @Test
    void update_shouldKeepCategoryUnchanged_whenNameIsBlank() {
        Category category = new Category("Food", CategoryType.VARIABLE);

        assertThatThrownBy(() -> category.update(" ", CategoryType.FIXED))
                .isInstanceOf(BusinessRuleViolationException.class);
        assertThat(category.getName()).isEqualTo("Food");
        assertThat(category.getType()).isEqualTo(CategoryType.VARIABLE);
    }

    @Test
    void update_shouldKeepCategoryUnchanged_whenTypeIsNull() {
        Category category = new Category("Food", CategoryType.VARIABLE);

        assertThatThrownBy(() -> category.update("Rent", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("type must not be null");
        assertThat(category.getName()).isEqualTo("Food");
    }
}
