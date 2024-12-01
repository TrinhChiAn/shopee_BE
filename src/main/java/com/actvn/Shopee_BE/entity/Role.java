package com.actvn.Shopee_BE.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    String roleId;
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private AppRole roleName;
    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
