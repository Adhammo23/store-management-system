package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.CategoryRequestDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }
    @GetMapping("/{categoryId}")
    public Category findById(@PathVariable Long categoryId) {
        return categoryService.findById(categoryId);
    }
    @PostMapping
    public Category addCategory(@Valid @RequestBody CategoryRequestDto dto) {
        return categoryService.addCategory(dto);
    }
}
