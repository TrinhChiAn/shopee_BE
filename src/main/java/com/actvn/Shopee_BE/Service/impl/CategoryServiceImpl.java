package com.actvn.Shopee_BE.Service.impl;

import com.actvn.Shopee_BE.Mapper.EntityDtoMapper;
import com.actvn.Shopee_BE.Repository.CategoryRepository;
import com.actvn.Shopee_BE.Service.CategoryService;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.CategoryResponse;
import com.actvn.Shopee_BE.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EntityDtoMapper mapper;

    @Override
    public ApiResponse getAllCategories() {

        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> list = categories.stream()
                .map(mapper::mapCategoryToDto)
                .collect(Collectors.toList());

        return ApiResponse
                .builder()
                .status(1000)
                .result(list)
                .build();
    }
}
