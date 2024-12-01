package com.actvn.Shopee_BE.Repository;

import com.actvn.Shopee_BE.entity.Category;
import com.actvn.Shopee_BE.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findProductByProductNameLike(String key, Pageable pageable);
}
