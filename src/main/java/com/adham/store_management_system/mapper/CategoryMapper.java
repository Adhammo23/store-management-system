package com.adham.store_management_system.mapper;

import com.adham.store_management_system.dto.CategoryRequestDto;
import com.adham.store_management_system.dto.CategoryResponseDto;
import com.adham.store_management_system.entity.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryRequestDto dto){

        if(dto==null) {
            return null;}

        Category category = new Category();

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public static CategoryResponseDto toResponse(Category category){
        if (category==null){
            return null;
        }
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
