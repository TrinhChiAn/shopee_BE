package com.actvn.Shopee_BE.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "product_name")
    String productName;
    String description;
    double discount;
    double price;
    String image;
    int quantity;

    @Column(name = "special_price")
    private double specialPrice;
    @Column(name = "Create_at")
    private final LocalDateTime createAt = LocalDateTime.now();
    @Column(name = "update_at")
    private LocalDateTime updateAt = LocalDateTime.now();
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = ("seller_id"))
    private User user;

    @OneToMany(
            mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    List<CartItem> products = new ArrayList<>();
}
