package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok(productService.findAll(page, size, sortBy));
    }

    @GetMapping("/{productID}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long productID) {

        return ResponseEntity.ok(productService.findById(productID));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(dto));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long productId,
                                                     @Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.updateProductById(productId, dto));
    }

    @PutMapping("/{productId}/stock")
    public ResponseEntity<ProductResponseDto> restockProduct(@PathVariable Long productId,
                                                             @RequestParam Integer quantity) {
        return ResponseEntity.ok(productService.restock(productId, quantity));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
