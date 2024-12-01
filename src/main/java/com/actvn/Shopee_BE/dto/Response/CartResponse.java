package com.actvn.Shopee_BE.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String cartId;
    Double totalPrice = 0.0;
    List<ProductItemResponse> productItemResponses = new ArrayList<>();
}
