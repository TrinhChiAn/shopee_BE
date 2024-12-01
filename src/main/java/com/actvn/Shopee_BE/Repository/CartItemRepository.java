package com.actvn.Shopee_BE.Repository;

import com.actvn.Shopee_BE.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    @Query(
            "SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.id = ?2"
    )
    CartItem findCartItemByProductIdAndCartId(String cartId, String productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.id = ?2" )
    void deleteItemByProductIdAndCartId(String cartId, String productId);
}
