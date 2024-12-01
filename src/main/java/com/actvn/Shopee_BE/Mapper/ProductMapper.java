package com.actvn.Shopee_BE.Mapper;

import com.actvn.Shopee_BE.dto.Request.ProductRequest;
import com.actvn.Shopee_BE.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest productRequest);
}
