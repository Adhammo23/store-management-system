package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.CategoryRequestDto;
import com.adham.store_management_system.dto.CategoryResponseDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.service.CategoryService;
import com.adham.store_management_system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.findById(categoryId));
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<Page<ProductResponseDto>> findByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(productService.findAllByCategoryId(categoryId, page, size, sortBy));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> addCategory(@Valid @RequestBody CategoryRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(dto));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@Valid @RequestBody CategoryRequestDto dto,
                                                              @PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, dto));
    }
}
