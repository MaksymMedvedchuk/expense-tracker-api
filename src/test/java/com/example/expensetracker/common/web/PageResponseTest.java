package com.example.expensetracker.common.web;

import com.example.expensetracker.common.Page;
import com.example.expensetracker.common.PageRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    void from_shouldMapElementsAndCopyPageMetadata() {
        Page<Integer> page = new Page<>(List.of(1, 2), new PageRequest(1, 2), 5);

        PageResponse<String> response = PageResponse.from(page, number -> "item-" + number);

        assertThat(response.content()).containsExactly("item-1", "item-2");
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.totalElements()).isEqualTo(5);
        assertThat(response.totalPages()).isEqualTo(3);
    }
}
