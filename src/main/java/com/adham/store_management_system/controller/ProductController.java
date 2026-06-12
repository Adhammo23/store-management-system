package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductResponseDto> findAll(
            @RequestParam(defaultValue = "0" ) int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return productService.findAll(page, size, sortBy);
    }
    @GetMapping("/{productID}")
    public ProductResponseDto findById(@PathVariable Long productID) {

        return productService.findById(productID);
    }
    @PostMapping
    public ProductResponseDto addProduct(@Valid @RequestBody ProductRequestDto dto) {
        return productService.addProduct(dto);
    }
    @PutMapping("/{productId}")
    public ProductResponseDto update(@Valid @PathVariable Long productId,
                          @RequestBody ProductRequestDto dto) {

    return productService.updateProductById(productId,dto);
    }
    @PutMapping("/{productId}/stock")
    public ProductResponseDto restockProduct(@PathVariable Long productId, @RequestParam Integer quantity){
        return productService.restock(productId,quantity);
    }
    @DeleteMapping("/{productId}")
    public void deleteById(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}
