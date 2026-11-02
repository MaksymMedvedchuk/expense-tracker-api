package com.example.expensetracker.category;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    void equalsShouldReturnTrueWhenNamesAreEqual() {
        Category first = categoryNamed("Food");
        Category second = categoryNamed("Food");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    private static Category categoryNamed(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
