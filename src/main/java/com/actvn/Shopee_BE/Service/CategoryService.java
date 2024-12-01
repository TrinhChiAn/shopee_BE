package com.actvn.Shopee_BE.Service;

import com.actvn.Shopee_BE.dto.Request.CategoryRequest;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.entity.Category;

public interface CategoryService {
    ApiResponse getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ApiResponse createNewCategory(CategoryRequest categoryRequest);

    Category getNewCategoryById(String id);

    ApiResponse<Object> updateCategoryById(String id, CategoryRequest categoryRequest);

    ApiResponse<Object> deleteCategoryById(String id);
}
