package com.actvn.Shopee_BE.Controller;

import com.actvn.Shopee_BE.Service.CategoryService;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/public/categories")
    public ApiResponse getCategories(){
        return categoryService.getAllCategories();
    }

}
