package com.actvn.Shopee_BE.Repository;

import com.actvn.Shopee_BE.entity.Cart;
import com.actvn.Shopee_BE.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.cartId = ?2")
    Cart findCartByEmailAndId(String email, String id);
}
