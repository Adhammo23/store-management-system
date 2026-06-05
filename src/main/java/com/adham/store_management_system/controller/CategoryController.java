package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.CategoryRequestDto;
import com.adham.store_management_system.dto.CategoryResponseDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.service.CategoryService;
import com.adham.store_management_system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }
    @GetMapping("/{categoryId}")
    public Category findById(@PathVariable Long categoryId) {
        return categoryService.findById(categoryId);
    }

    @GetMapping("/{categoryId}/products")
    public Page<ProductResponseDto> findByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0" ) int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return productService.findAllByCategoryId(categoryId,page,size,sortBy);
    }
    @PostMapping
    public CategoryResponseDto addCategory(@Valid @RequestBody CategoryRequestDto dto) {
        return categoryService.addCategory(dto);
    }
}
