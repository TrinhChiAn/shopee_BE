package com.actvn.Shopee_BE.Repository;

import com.actvn.Shopee_BE.entity.AppRole;
import com.actvn.Shopee_BE.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(AppRole appRole);
}
