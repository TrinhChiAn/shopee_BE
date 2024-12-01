package com.actvn.Shopee_BE.Service;

import com.actvn.Shopee_BE.dto.Request.ProductRequest;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.PageResponse;
import com.actvn.Shopee_BE.dto.Response.ProductItemResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ApiResponse<Object> createNewProduct(String categoryId, ProductRequest productRequest);

    ApiResponse<Object> updateProductImage(String productId, MultipartFile image);

    PageResponse getAllProductsByCategoryId(String categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    PageResponse<ProductItemResponse> getProductByProductNameLike(String key, int pageNumber, int pageSize, String sortBy, String sortOrder);

}
