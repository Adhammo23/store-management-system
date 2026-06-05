package com.adham.store_management_system.service;

import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.entity.Product;
import com.adham.store_management_system.exception.ResourceNotFoundException;
import com.adham.store_management_system.mapper.ProductMapper;
import com.adham.store_management_system.repository.CategoryRepository;
import com.adham.store_management_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public Page<ProductResponseDto> findAll(int page , int size , String sortBy) {
        Pageable pageable = PageRequest.of(page , size, Sort.by(sortBy));
        return productRepository.findAll(pageable)
                .map(ProductMapper::toResponse);
    }

    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return ProductMapper.toResponse(product);
    }

    public ProductResponseDto addProduct(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = ProductMapper.toEntity(dto, category);
        Product saveProduct = productRepository.save(product);
        return ProductMapper.toResponse(saveProduct);
    }

    public ProductResponseDto updateProductById(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);

        Product updateProduct = productRepository.save(product);

        return ProductMapper.toResponse(updateProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    public Page<ProductResponseDto> findAllByCategoryId(Long categoryId, int page, int size, String sortBy) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Pageable pageable = PageRequest.of(page , size, Sort.by(sortBy));
        return productRepository.findByCategoryId(categoryId,pageable)
                .map(ProductMapper::toResponse);
    }
}
