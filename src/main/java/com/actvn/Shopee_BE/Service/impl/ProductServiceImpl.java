package com.actvn.Shopee_BE.Service.impl;

import com.actvn.Shopee_BE.Mapper.ProductMapper;
import com.actvn.Shopee_BE.Repository.CategoryRepository;
import com.actvn.Shopee_BE.Repository.ProductRepository;
import com.actvn.Shopee_BE.Service.FileService;
import com.actvn.Shopee_BE.Service.ProductService;
import com.actvn.Shopee_BE.common.Constants;
import com.actvn.Shopee_BE.dto.Request.ProductRequest;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.PageResponse;
import com.actvn.Shopee_BE.dto.Response.ProductItemResponse;
import com.actvn.Shopee_BE.entity.Category;
import com.actvn.Shopee_BE.entity.Product;
import com.actvn.Shopee_BE.exception.ErrorCode;
import com.actvn.Shopee_BE.exception.NotFoundCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse<Object> createNewProduct(String categoryId, ProductRequest productRequest) {

        Category category = getCategoryById(categoryId);

        double special_price = productRequest.getPrice()
                - (productRequest.getDiscount()*0.01)*productRequest.getPrice();

        Product product = modelMapper.map(productRequest,Product.class);
        product.setCategory(category);
        product.setSpecialPrice(special_price);

        Product saveProduct = productRepository.save(product);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Category created successfully")
                .result(saveProduct)
                .build();
    }


    @Override
    public ApiResponse<Object> updateProductImage(String productId, MultipartFile image) {
        Product product = getProductById(productId);

        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);

        Product saveProduct = productRepository.save(product);
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("update image successfully")
                .result(modelMapper.map(saveProduct, ProductItemResponse.class))
                .build();
    }

    @Override
    public PageResponse getAllProductsByCategoryId(String categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Category category = getCategoryById(categoryId);

        Sort sortByAndOrder = sortOrder.equals(Constants.CATEGORY_SORT_ORDER)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Product> productsPage = productRepository.findByCategory(category, pageable);

        List<ProductItemResponse> products = productsPage.stream()
                .map(item -> modelMapper.map(item, ProductItemResponse.class))
                .toList();

        return PageResponse.<ProductItemResponse>builder()
                .context(products)
                .pageNumber(productsPage.getNumber()+1)
                .pageSize(productsPage.getSize())
                .totalElements(productsPage.getTotalElements())
                .totalPages(productsPage.getTotalPages())
                .lastPage(productsPage.isLast())
                .firstPage(productsPage.isFirst())
                .build();
    }

    @Override
    public PageResponse<ProductItemResponse> getProductByProductNameLike(String key, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals(Constants.CATEGORY_SORT_ORDER)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        String keyword = "%" + key + "%";
        Page<Product> productsPage = productRepository.findProductByProductNameLike(keyword, pageable);

        List<ProductItemResponse> productItemResponses = productsPage.stream().map(item -> modelMapper.map(item, ProductItemResponse.class)).toList();

        return PageResponse.<ProductItemResponse>builder()
                .context(productItemResponses)
                .pageNumber(productsPage.getNumber()+1)
                .pageSize(productsPage.getSize())
                .totalElements(productsPage.getTotalElements())
                .totalPages(productsPage.getTotalPages())
                .lastPage(productsPage.isLast())
                .firstPage(productsPage.isFirst())
                .build();
    }


    @Override
    public Product getProductById(String productId){
        return productRepository.findById(productId).orElseThrow(() ->
                new NotFoundCategory(ErrorCode.USER_NOT_EXISTED));
    }


    public Category getCategoryById(String id) {

        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundCategory(ErrorCode.USER_NOT_EXISTED));
    }
}
