package com.actvn.Shopee_BE.Service.impl;

import com.actvn.Shopee_BE.Repository.RoleRepository;
import com.actvn.Shopee_BE.Service.RoleService;
import com.actvn.Shopee_BE.dto.Request.UserRequest;
import com.actvn.Shopee_BE.entity.AppRole;
import com.actvn.Shopee_BE.entity.Role;
import com.actvn.Shopee_BE.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Optional<Role> findByRoleName(AppRole appRole) {
        return roleRepository.findByRoleName(appRole);
    }


}
