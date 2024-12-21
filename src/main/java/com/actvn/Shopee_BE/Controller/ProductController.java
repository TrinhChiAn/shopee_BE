package com.actvn.Shopee_BE.Controller;

import com.actvn.Shopee_BE.Service.FileService;
import com.actvn.Shopee_BE.Service.ProductService;
import com.actvn.Shopee_BE.common.Constants;
import com.actvn.Shopee_BE.dto.Request.ProductRequest;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.PageResponse;
import com.actvn.Shopee_BE.dto.Response.ProductItemResponse;
import com.actvn.Shopee_BE.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ApiResponse<Object>> createNewProduct(
            @PathVariable String categoryId,
            @RequestBody ProductRequest productRequest
    ){
        return ResponseEntity.ok().body(productService.createNewProduct(categoryId,productRequest));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id){
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<PageResponse> getProductsByCategoryId(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.PRODUCT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = Constants.SORT_BY_ORDER, required = false) String sortOrder
    ){
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryId(categoryId,pageNumber,pageSize,sortBy,sortOrder));
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<PageResponse<ProductItemResponse>> getProductByKeyword(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber",defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.PRODUCT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = Constants.SORT_BY_ORDER, required = false) String sortOrder
    ){
        return ResponseEntity.ok().body(productService.getProductByProductNameLike(keyword,pageNumber,pageSize,sortBy,sortOrder));
    }


    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ApiResponse<Object>> updateProductImage(
            @PathVariable String productId,
            @RequestParam("image") MultipartFile image
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProductImage(productId, image));
    }
}
