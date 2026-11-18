package com.example.expensetracker.category.web;

import com.example.expensetracker.category.Category;
import com.example.expensetracker.category.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest request) {
        Category created = categoryService.create(request.name(), request.type());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(categoryMapper.toResponse(created));
    }

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryResponse findById(@PathVariable UUID id) {
        return categoryMapper.toResponse(categoryService.findById(id));
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        return categoryMapper.toResponse(categoryService.update(id, request.name(), request.type()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        categoryService.delete(id);
    }
}
