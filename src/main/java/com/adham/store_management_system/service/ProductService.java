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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductResponseDto> findAll(int page , int size , String sortBy) {
        log.debug("Fetching products. page: {}, size: {}, sortBy: {}", page, size, sortBy);
        Pageable pageable = PageRequest.of(page , size, Sort.by(sortBy));
        return productRepository.findAll(pageable)
                .map(ProductMapper::toResponse);
    }

    @Cacheable(value = "products::item"
            , key = "#id")
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("product not found with Id: {}",id);
                   return new ResourceNotFoundException("Product not found");
                });
        return ProductMapper.toResponse(product);
    }

    @CacheEvict(value = {"products::page", "products::category::page"}, allEntries = true)
    public ProductResponseDto addProduct(ProductRequestDto dto) {
        log.info("Creating product with name {}", dto.getName());
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->{
                    log.warn("Category not found with id:{}", dto.getCategoryId());
                return new ResourceNotFoundException("Category not found");});
        if (productRepository.existsByName(dto.getName())) {
            log.warn("Attempt to create duplicate product: {}", dto.getName());

            throw new IllegalArgumentException("Product already exists! To add more quantities, please use the Restock/Inventory endpoint.");
        }
        Product product = ProductMapper.toEntity(dto, category);
        category.addProduct(product);
        Product saveProduct = productRepository.save(product);
        log.info("Product created successfully with id {}", saveProduct.getId());
        return ProductMapper.toResponse(saveProduct);
    }

    @CachePut(value = "products::item", key = "#productId")
    public ProductResponseDto restock(Long productId, Integer quantity){
        log.info("Restock request received for product {} with quantity {}",productId,quantity);
        if (quantity <=0){

            log.warn("Invalid restock quantity {} for product {}",
                    quantity, productId);
            throw new IllegalArgumentException("Quantity to add must be greater than zero");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> {

                    log.warn("Product not found with id {}", productId);
                     return new ResourceNotFoundException( "product not found");});

        product.setStockQuantity(product.getStockQuantity() + quantity);
        Product updateStock = productRepository.save(product);
        log.info("Product {} restocked successfully. newStock: {}", productId, updateStock.getStockQuantity());
        return ProductMapper.toResponse(updateStock);
    }

    @CachePut(value = "products::item", key = "#id")
    public ProductResponseDto updateProductById(Long id, ProductRequestDto dto) {
        log.info("Updating product with id {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Product not found with id: {}", id);
                    return new ResourceNotFoundException("Product not found");});

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> {
                    log.warn("Category not found with id {}", dto.getCategoryId());
                    return new ResourceNotFoundException("Category not found");
                });

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);

        Product updateProduct = productRepository.save(product);
        log.info("Product {} updated successfully", id);

        return ProductMapper.toResponse(updateProduct);
    }


    @CacheEvict(value = "products::item", key = "#id")
    public void deleteProduct(Long id) {
        log.info("Deleting product with id {}",id);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Product not found with id {}", id);
                    return new ResourceNotFoundException("Product not found");
                });
        productRepository.delete(product);
        log.info("Product {} deleted successfully", id);
    }

    public Page<ProductResponseDto> findAllByCategoryId(Long categoryId, int page, int size, String sortBy) {
        log.debug("Fetching products by category. categoryId: {}, page: {}, size: {}, sortBy: {}", categoryId, page, size, sortBy);
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found with id {}", categoryId);
                   return new ResourceNotFoundException("Category not found");
                });

        Pageable pageable = PageRequest.of(page , size, Sort.by(sortBy));
        return productRepository.findByCategoryId(categoryId,pageable)
                .map(ProductMapper::toResponse);
    }
}
