package com.example.expensetracker.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageRequestTest {

    @Test
    void constructor_shouldThrowException_whenPageIsNegative() {
        assertThatThrownBy(() -> new PageRequest(-1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("page must not be negative, but was: -1");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void constructor_shouldThrowException_whenSizeIsNotPositive(int size) {
        assertThatThrownBy(() -> new PageRequest(0, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("size must be positive, but was: " + size);
    }

    @Test
    void offset_shouldSkipPreviousPages() {
        PageRequest request = new PageRequest(2, 10);

        assertThat(request.offset()).isEqualTo(20);
    }
}
