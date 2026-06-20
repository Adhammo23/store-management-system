package com.adham.store_management_system.service;

import com.adham.store_management_system.dto.CategoryRequestDto;
import com.adham.store_management_system.dto.CategoryResponseDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.exception.ResourceNotFoundException;
import com.adham.store_management_system.mapper.CategoryMapper;
import com.adham.store_management_system.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryService {
     private final CategoryRepository categoryRepository;

     @Cacheable(value = "category::all")
     public List<CategoryResponseDto> findAll() {
         return categoryRepository.findAll()
                 .stream()
                 .map(CategoryMapper::toResponse)
                 .toList() ;
     }
     @Cacheable(value = "category::item",key = "#id")
     public CategoryResponseDto findById(Long id) {
         Category category =  categoryRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

         return CategoryMapper.toResponse(category);
     }
     @CacheEvict(value = "category::all",allEntries = true)
     public CategoryResponseDto addCategory(CategoryRequestDto dto) {
         Category category = new Category();
         category.setName(dto.getName());
         category.setDescription(dto.getDescription());
         Category saveCategory = categoryRepository.save(category);
         return CategoryMapper.toResponse(saveCategory);
     }

    @CacheEvict(value = {
            "categories::all",
            "categories::item"
    }, allEntries = true)
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryDto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException( "Category not found"));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category updateCategory = categoryRepository.save(category);

        return CategoryMapper.toResponse(updateCategory);
    }
}
