package com.actvn.Shopee_BE.Service.impl;

import com.actvn.Shopee_BE.Mapper.EntityDtoMapper;
import com.actvn.Shopee_BE.Repository.CategoryRepository;
import com.actvn.Shopee_BE.Service.CategoryService;
import com.actvn.Shopee_BE.common.Constants;
import com.actvn.Shopee_BE.dto.Request.CategoryRequest;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.CategoryItemResponse;
import com.actvn.Shopee_BE.dto.Response.PageResponse;
import com.actvn.Shopee_BE.entity.Category;
import com.actvn.Shopee_BE.exception.ErrorCode;
import com.actvn.Shopee_BE.exception.NotFoundCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EntityDtoMapper mapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse<Object> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equals(Constants.CATEGORY_SORT_ORDER)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();

        List<CategoryItemResponse> list = categories.stream()
                .map(item -> modelMapper.map(item,CategoryItemResponse.class))
                .collect(Collectors.toList());

        PageResponse categoryResponse = PageResponse.<CategoryItemResponse>builder()
                .context(list)
                .pageNumber(categoryPage.getNumber()+1)
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .lastPage(categoryPage.isLast())
                .firstPage(categoryPage.isFirst())
                .build();


        return ApiResponse
                .builder()
                .status(HttpStatus.OK)
                .result(categoryResponse)
                .build();
    }

    @Override
    public ApiResponse<Object> createNewCategory(CategoryRequest categoryRequest) {
        Category category = new Category();

        category.setName(categoryRequest.getName());

        Category created = categoryRepository.save(category);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Category created successfully")
                .result(created)
                .build();
    }

    @Override
    public Category getNewCategoryById(String id) {

        Category foundCategory = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundCategory(ErrorCode.USER_NOT_EXISTED));

        return foundCategory;
    }

    @Override
    public ApiResponse<Object> updateCategoryById(String id, CategoryRequest categoryRequest) {
        Category foundCategory = this.getNewCategoryById(id);

        foundCategory.setName(categoryRequest.getName());
        categoryRepository.save(foundCategory);

        return ApiResponse.builder()
                .message("update success")
                .status(HttpStatus.OK)
                .build();

    }

    @Override
    public ApiResponse<Object> deleteCategoryById(String id) {
        Category foundCategory = this.getNewCategoryById(id);

        categoryRepository.delete(foundCategory);

        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("delete success")
                .build();
    }
}
