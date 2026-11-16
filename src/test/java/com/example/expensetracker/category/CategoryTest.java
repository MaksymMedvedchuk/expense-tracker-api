package com.example.expensetracker.category;

import com.example.expensetracker.common.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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
    void equals_shouldReturnTrue_whenSameInstance() {
        Category category = new Category("Food", CategoryType.VARIABLE);

        assertThat(category).isEqualTo(category);
        assertThat(category.hashCode()).isEqualTo(category.getId().hashCode());
    }
}
