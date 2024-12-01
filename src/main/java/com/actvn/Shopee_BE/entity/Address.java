package com.actvn.Shopee_BE.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    String addressId;
    @Column(name = "building_name")
    String buildingName;
    String city;
    String country;
    @Column(name = "pin_code")
    String pinCode;
    String state;
    String street;
    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    List<User> users = new ArrayList<>();

    public Address(String addressId, String city, String buildingName, String country, String pinCode, String state, String street) {
        this.addressId = addressId;
        this.city = city;
        this.buildingName = buildingName;
        this.country = country;
        this.pinCode = pinCode;
        this.state = state;
        this.street = street;
    }

}
