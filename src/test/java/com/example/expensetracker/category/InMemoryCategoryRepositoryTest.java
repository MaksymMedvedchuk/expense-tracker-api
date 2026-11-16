package com.example.expensetracker.category;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryCategoryRepositoryTest {

    private final InMemoryCategoryRepository repository = new InMemoryCategoryRepository();

    @Test
    void findById_shouldReturnSavedCategory() {
        Category food = repository.save(new Category("Food", CategoryType.VARIABLE));

        assertThat(repository.findById(food.getId())).containsSame(food);
    }

    @Test
    void findById_shouldReturnEmpty_whenCategoryIsMissing() {
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void existsByNameIgnoreCase_shouldIgnoreCase() {
        repository.save(new Category("Food", CategoryType.VARIABLE));

        assertThat(repository.existsByNameIgnoreCase("fOOd")).isTrue();
        assertThat(repository.existsByNameIgnoreCase("Rent")).isFalse();
    }

    @Test
    void findAll_shouldReturnCategoriesSortedByName() {
        Category rent = repository.save(new Category("Rent", CategoryType.FIXED));
        Category food = repository.save(new Category("Food", CategoryType.VARIABLE));

        assertThat(repository.findAll()).containsExactly(food, rent);
    }

    @Test
    void existsByNameIgnoreCaseAndIdNot_shouldIgnoreCategoryWithGivenId() {
        Category food = repository.save(new Category("Food", CategoryType.VARIABLE));
        Category rent = repository.save(new Category("Rent", CategoryType.FIXED));

        assertThat(repository.existsByNameIgnoreCaseAndIdNot("FOOD", food.getId())).isFalse();
        assertThat(repository.existsByNameIgnoreCaseAndIdNot("FOOD", rent.getId())).isTrue();
    }

    @Test
    void deleteById_shouldRemoveCategory() {
        Category food = repository.save(new Category("Food", CategoryType.VARIABLE));

        repository.deleteById(food.getId());

        assertThat(repository.findById(food.getId())).isEmpty();
    }
}
