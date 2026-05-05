package com.adham.store_management_system.controller;

import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.entity.Product;
import com.adham.store_management_system.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public List<ProductResponseDto> findAll() {
        return productService.findAll();
    }
    @GetMapping("/{productID}")
    public ProductResponseDto findById(@PathVariable Long productID) {

        return productService.findById(productID);
    }
    @PostMapping
    public Product addProduct(@Valid @RequestBody ProductRequestDto dto) {
        return productService.addProduct(dto);
    }
    @PatchMapping("/{productId}")
    public Product update(@Valid @PathVariable Long productId,
                          @RequestBody ProductRequestDto dto) {

    return productService.updateProductById(productId,dto);
    }
    @DeleteMapping("/{productId}")
    public void deleteById(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

}
