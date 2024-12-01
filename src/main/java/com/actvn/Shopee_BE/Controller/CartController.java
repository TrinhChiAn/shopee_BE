package com.actvn.Shopee_BE.Controller;

import com.actvn.Shopee_BE.Service.CartService;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.CartResponse;
import com.actvn.Shopee_BE.ultil.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private CartService cartService;

    @PostMapping("/product/{productId}/quantity/{quantity}")
    public ResponseEntity<ApiResponse<Object>> addProductToCart(@PathVariable String productId,
                                                                @PathVariable int quantity) {
        return ResponseEntity.ok().body(cartService.addProductToCart(productId, quantity));
    }

    @PutMapping("/product/{productId}/quantity/{quantity}")
    public void updateCartProduct(@PathVariable String productId,
                                  @PathVariable int quantity) {

    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<ApiResponse<Object>> deleteProductFromCart(@PathVariable String cartId,
                                                                     @PathVariable String productId) {
       return ResponseEntity.ok().body(cartService.deleteProductFromCart(cartId, productId));
    }

    @GetMapping("/user/cart")
    public ResponseEntity<ApiResponse<Object>> getCartById() {
        return ResponseEntity.ok().body(cartService.getCartById());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartResponse>>> getAllCart() {
        return ResponseEntity.ok().body(cartService.getAllCarts());
    }
}
