package com.actvn.Shopee_BE.Controller;

import com.actvn.Shopee_BE.Service.CategoryService;
import com.actvn.Shopee_BE.common.Constants;
import com.actvn.Shopee_BE.dto.Request.CategoryRequest;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.entity.Category;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/public/categories")
    public ApiResponse getCategories(
            @RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = Constants.CATEGORY_SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = Constants.CATEGORY_SORT_ORDER) String sortOrder
    ){
        return categoryService.getAllCategories(pageNumber,pageSize, sortBy, sortOrder);
    }

    @PostMapping("admin/categories")
    public ApiResponse createNewCategory(@RequestBody @Valid CategoryRequest categoryRequest){
        return categoryService.createNewCategory(categoryRequest);
    }

    @GetMapping("public/categories/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable String id){
        Category category = categoryService.getNewCategoryById(id);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK)
                .result(category)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("admin/categories/{id}")
    public ResponseEntity<ApiResponse<Object>> updateCategoryById(@PathVariable String id, @RequestBody CategoryRequest dto){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategoryById(id,dto));
    }

    @DeleteMapping("admin/categories/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteCategoryById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategoryById(id));
    }

}