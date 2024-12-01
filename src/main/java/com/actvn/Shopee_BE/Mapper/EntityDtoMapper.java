package com.actvn.Shopee_BE.Mapper;

import com.actvn.Shopee_BE.dto.Response.CategoryItemResponse;
import com.actvn.Shopee_BE.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {
    // Category to DTO
    public CategoryItemResponse mapCategoryToDto(Category entity){

        CategoryItemResponse categoryResponse = new CategoryItemResponse();

        categoryResponse.setId(entity.getId());
        categoryResponse.setName(entity.getName());

        return categoryResponse;
    }
}
