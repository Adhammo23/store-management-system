package com.adham.store_management_system.mapper;

import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        return  product;
    }
}
