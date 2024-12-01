package com.actvn.Shopee_BE.Service;

import com.actvn.Shopee_BE.dto.Request.UserRequest;
import com.actvn.Shopee_BE.entity.AppRole;
import com.actvn.Shopee_BE.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RoleService {
    Optional<Role> findByRoleName(AppRole appRole);
}
