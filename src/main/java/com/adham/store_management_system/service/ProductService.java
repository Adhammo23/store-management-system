package com.adham.store_management_system.service;

import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.entity.Product;
import com.adham.store_management_system.exception.ResourceNotFoundException;
import com.adham.store_management_system.mapper.ProductMapper;
import com.adham.store_management_system.repository.CategoryRepository;
import com.adham.store_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductResponseDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return ProductMapper.toResponse(product);
    }

    public Product addProduct(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = ProductMapper.toEntity(dto, category);
        return productRepository.save(product);
    }

    public Product updateProductById(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    public List<ProductResponseDto> findAllByCategoryId(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }
}
