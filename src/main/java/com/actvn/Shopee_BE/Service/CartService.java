package com.actvn.Shopee_BE.Service;

import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.CartResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    ApiResponse<Object> addProductToCart(String productId, Integer quantity);

    ApiResponse<Object> updateProductInCarts(String productId, Integer quantity);

    void updateProductInCart(String cartId, String productId);

    ApiResponse<Object> deleteProductFromCart(String cartId, String productId);

    ApiResponse<Object> getCartById();

    ApiResponse<List<CartResponse>> getAllCarts();
}
