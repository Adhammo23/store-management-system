package com.adham.store_management_system.service;
import com.adham.store_management_system.dto.ProductRequestDto;
import com.adham.store_management_system.dto.ProductResponseDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.entity.Product;
import com.adham.store_management_system.exception.ResourceNotFoundException;
import com.adham.store_management_system.repository.CategoryRepository;
import com.adham.store_management_system.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void addProduct_whenValidInput_shouldReturnProductResponse() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        ProductRequestDto dto = ProductRequestDto.builder()
                .name("iphone 15")
                .price(new BigDecimal("999.99"))
                .categoryId(1L)
                .stockQuantity(50)
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name("iphone 15")
                .price(new BigDecimal( "999.99"))
                .category(category)
                .stockQuantity(50)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsByName("iphone 15")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDto responseDto = productService.addProduct(dto);


        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getProductName()).isEqualTo("iphone 15");
        assertThat(responseDto.getProductPrice()).isEqualByComparingTo("999.99");
        assertThat(responseDto.getCategoryName()).isEqualTo("Electronics");

        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void addProduct_whenNameAlreadyExists_shouldThrowException(){

        Category category = new Category();
        category.setId(1L);

        ProductRequestDto dto = ProductRequestDto.builder()
                .name("iphone 15")
                .price(new BigDecimal("999.99"))
                .categoryId(1L)
                .stockQuantity(50)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsByName("iphone 15")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(dto));

        verify(productRepository,never()).save(any());
    }

    @Test
    void addProduct_whenCategoryNotFound_shouldThrowResourceNotFoundException(){

        ProductRequestDto dto = ProductRequestDto.builder()
                .name("iphone 15")
                .categoryId(1L)
                .price(new BigDecimal("999.99"))
                .stockQuantity(50)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> productService.addProduct(dto));

        verify(productRepository,never()).save(any());
    }

    @Test
    void findById_whenProductExists_shouldReturnProductResponse(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = Product.builder()
                .id(1L)
                .name("iphone 15")
                .price(new BigDecimal("999.99"))
                .category(category)
                .stockQuantity(50)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDto dto = productService.findById(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isEqualTo(1L);
        assertThat(dto.getCategoryName()).isEqualTo(category.getName());
        assertThat(dto.getProductPrice()).isEqualByComparingTo("999.99");
        assertThat(dto.getStockQuantity()).isEqualTo(50);

        verify(productRepository,times(1)).findById(1L);
    }

    @Test
    void findById_whenProductNotFound_shouldThrowResourceNotFoundException(){

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()-> productService.findById(1L));

        verify(productRepository,times(1)).findById(1L);
    }

    @Test
    void restock_whenValidInput_shouldReturnUpdatedStock (){

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("iphone 15")
                .price(new BigDecimal("999.99"))
                .category(category)
                .stockQuantity(50)
                .build();

        Product updatedProduct = Product.builder()
                .id(1L)
                .name("iphone 15")
                .price(new BigDecimal("999.99"))
                .category(category)
                .stockQuantity(120) // 50 + 70
                .build();


        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(updatedProduct);
        ProductResponseDto dto = productService.restock(product.getId(),70);

        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isEqualTo(product.getId());
        assertThat(dto.getStockQuantity()).isEqualTo(120);


        verify(productRepository,times(1)).save(product);
    }

    @Test
    void restock_whenQuantityIsZero_shouldThrowIllegalArgumentException(){

        assertThrows(IllegalArgumentException.class,()-> productService.restock(1L,0));

        verify(productRepository,never()).save(any());
    }

    @Test
    void restock_whenProductNotFound_shouldThrowResourceNotFoundException() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()-> productService.restock(1L,20));

        verify(productRepository,never()).save(any());
    }
}
