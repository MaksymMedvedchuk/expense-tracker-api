package com.example.expensetracker.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageTest {

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 1", "10, 1", "11, 2"})
    void totalPages_shouldCountLastIncompletePage(long totalElements, long expectedPages) {
        Page<String> page = new Page<>(List.of(), new PageRequest(0, 10), totalElements);

        assertThat(page.totalPages()).isEqualTo(expectedPages);
    }

    @Test
    void content_shouldNotChange_whenSourceListIsModified() {
        List<String> source = new ArrayList<>(List.of("a"));
        Page<String> page = new Page<>(source, new PageRequest(0, 10), 1);

        source.add("b");

        assertThat(page.content()).containsExactly("a");
    }

    @Test
    void constructor_shouldThrowException_whenTotalElementsIsNegative() {
        assertThatThrownBy(() -> new Page<>(List.of(), new PageRequest(0, 10), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("totalElements must not be negative, but was: -1");
    }
}
