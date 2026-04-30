package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.CategoryRequestDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.service.CategoryService;
import com.adham.store_management_system.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }
    @GetMapping("/{categoryId}")
    public Category findById(@PathVariable Long categoryId) {
        return categoryService.findById(categoryId);
    }

    @GetMapping("/{categoryId}/products")
    public List<ProductResponseDto> findByCategory(@PathVariable Long categoryId) {
        return productService.findAllByCategoryId(categoryId);
    }

    @PostMapping
    public Category addCategory(@Valid @RequestBody CategoryRequestDto dto) {
        return categoryService.addCategory(dto);
    }
}
