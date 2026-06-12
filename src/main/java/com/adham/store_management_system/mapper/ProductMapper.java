package com.adham.store_management_system.mapper;

import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.entity.Product;

public class ProductMapper {
    // return product from client to entity
    public static Product toEntity(ProductRequestDto dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setStockQuantity(dto.getStockQuantity());
        return  product;
    }
    // return product from entity to client
    public static ProductResponseDto toResponse(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductPrice(product.getPrice());
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }
}
