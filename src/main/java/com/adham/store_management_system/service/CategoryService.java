package com.adham.store_management_system.service;

import com.adham.store_management_system.dto.CategoryRequestDto;
import com.adham.store_management_system.entity.Category;
import com.adham.store_management_system.exception.ResourceNotFoundException;
import com.adham.store_management_system.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService {
     private final CategoryRepository categoryRepository;

     public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
     }
     public List<Category> findAll() {
           return categoryRepository.findAll();
     }
     public Category findById(Long id) {
         return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
     }
     public Category addCategory( CategoryRequestDto dto) {
         Category category = new Category();
         category.setName(dto.getName());
         category.setDescription(dto.getDescription());
         return categoryRepository.save(category);
     }

}
