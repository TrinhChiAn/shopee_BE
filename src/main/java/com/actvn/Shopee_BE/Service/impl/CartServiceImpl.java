package com.actvn.Shopee_BE.Service.impl;

import com.actvn.Shopee_BE.Repository.CartItemRepository;
import com.actvn.Shopee_BE.Repository.CartRepository;
import com.actvn.Shopee_BE.Repository.ProductRepository;
import com.actvn.Shopee_BE.Service.CartService;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.CartResponse;
import com.actvn.Shopee_BE.dto.Response.ProductItemResponse;
import com.actvn.Shopee_BE.entity.Cart;
import com.actvn.Shopee_BE.entity.CartItem;
import com.actvn.Shopee_BE.entity.Product;
import com.actvn.Shopee_BE.exception.ApiException;
import com.actvn.Shopee_BE.ultil.AuthUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Log4j2
public class CartServiceImpl implements CartService {

    final ModelMapper mapper;

    final CartItemRepository cartItemRepository;

    final ProductRepository productRepository;

    final CartRepository cartRepository;

    final AuthUtils authUtils;

    @SneakyThrows
    @Override
    public ApiResponse<Object> addProductToCart(String productId, Integer quantity) {
        Cart cart = createCart();
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new RuntimeException("not found product")
        );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(), productId
        );

//        if (cartItem != null){
//            throw new ApiException("Product with product name: "+product.getProductName()+" already exits in the cart!");
//        }
//
//        if (product.getQuantity()==0){
//            throw new ApiException("Product with product name: "+product.getProductName()+" out of stock!");
//        }
//
//        if (quantity > product.getQuantity()){
//            throw new ApiException("Please, make an order of the product name: "+product.getProductName()+
//                    " less than or equal to the quantity: "+product.getQuantity());
//        }

        CartItem newCartitem = new CartItem();
        newCartitem.setProduct(product);
        newCartitem.setQuantity(quantity);
        newCartitem.setCart(cart);
        newCartitem.setDiscount(product.getDiscount());
        newCartitem.setPrice(product.getSpecialPrice());
        newCartitem.setTotalPrice((product.getSpecialPrice()) * quantity);

        cartItemRepository.save(newCartitem);

        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice()) * quantity);
        cartRepository.save(cart);

        CartResponse cartResponse = mapper.map(cart, CartResponse.class);
        List<CartItem> cartItems = cart.getCartItems();
        log.info(cartItems);
        Stream<ProductItemResponse> responseStream = cartItems.stream().map(item -> {
            ProductItemResponse map = mapper.map(item.getProduct(), ProductItemResponse.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartResponse.setProductItemResponses(responseStream.toList());
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message(" Cart Created successfully")
                .result(cartResponse)
                .build();

    }


    @Override
    public void updateProductInCart(String cartId, String productId) {

    }

    @Override
    public ApiResponse<Object> deleteProductFromCart(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("loi"));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ApiException("Can not found product with product_id");
        }

        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice() * cartItem.getQuantity());

        cartItemRepository.deleteItemByProductIdAndCartId(cartId, productId);

        cartRepository.save(cart);
        return ApiResponse.builder().message("oke").status(HttpStatus.OK).result(true).build();
    }

    @Override
    public ApiResponse<Object> getCartById() {
        String email = authUtils.getEmailLogged();
        Cart cart = cartRepository.findCartByEmail(email);
        String cartId = cart.getCartId();

        return ApiResponse.builder().message("Oke").status(HttpStatus.OK).result(getCart(email, cartId)).build();

    }

    private CartResponse getCart(String email, String id) {
        Cart cart = cartRepository.findCartByEmailAndId(email, id);
        if (cart == null) {
            throw new ApiException("Cart " + id + " not found!");
        }

        CartResponse cartResponse = mapper.map(cart, CartResponse.class);

        List<ProductItemResponse> productItemResponses = cart.getCartItems().stream()
                .map(p -> mapper.map(p, ProductItemResponse.class)).toList();

        cartResponse.setProductItemResponses(productItemResponses);
        return cartResponse;

    }

    @Override
    public ApiResponse<List<CartResponse>> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new ApiException("No cart exists!");
        }

        List<CartResponse> cartResponses = carts.stream().map(item -> {
            CartResponse cartDRO = mapper.map(item, CartResponse.class);
            List<ProductItemResponse> productRepositories = item.getCartItems().stream().map(product ->
                    mapper.map(product.getProduct(), ProductItemResponse.class)
            ).toList();
            cartDRO.setProductItemResponses(productRepositories);
            return cartDRO;
        }).toList();
        return ApiResponse.<List<CartResponse>>builder()
                .status(HttpStatus.OK)
                .message("get all cart successfully")
                .result(cartResponses)
                .build();
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtils.getEmailLogged());
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setUser(authUtils.getUserLogged());

        return cartRepository.save(cart);
    }

    @Override
    public ApiResponse<Object> updateProductInCarts(String productId, Integer quantity) {
        String email = authUtils.getEmailLogged();
        Cart userCart = cartRepository.findCartByEmail(email);
        String cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ApiException("Cart " + cartId + " not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Product " + productId + " not found"));

        if (product.getQuantity() == 0) {
            throw new ApiException("Product " + product.getProductName() + " is sold out");
        }

        if (product.getQuantity() < quantity) {
            throw new ApiException("Product" + product.getProductName() + "is less or equal than" + product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ApiException("Product with name: " + product.getProductName() + " not available in the cart!");
        }

        int newQuantity = cartItem.getQuantity() + quantity;
        if (newQuantity < 0) {
            throw new ApiException("The result quantity cannot be negative!");
        }
        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setQuantity(newQuantity);
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getPrice() * quantity));

            cartRepository.save(cart);
        }

        CartItem updateItem = cartItemRepository.save(cartItem);
        if (updateItem.getQuantity() == 0) {
            cartRepository.deleteById(updateItem.getCartItemId());
        }

        CartResponse cartDTO = mapper.map(cart, CartResponse.class);
        List<CartItem> cartItems = cart.getCartItems();
        List<ProductItemResponse> productItemResponses = cartItems.stream().map(item -> {
            ProductItemResponse dto = mapper.map(item.getProduct(), ProductItemResponse.class);
            dto.setQuantity(item.getQuantity());
            return dto;
        }).toList();

        cartDTO.setProductItemResponses(productItemResponses);
        return ApiResponse.builder().message("oke").status(HttpStatus.OK).result(cartDTO).build();
    }
}
