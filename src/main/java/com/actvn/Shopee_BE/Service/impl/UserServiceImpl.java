package com.actvn.Shopee_BE.Service.impl;

import com.actvn.Shopee_BE.Repository.UserRepository;
import com.actvn.Shopee_BE.Service.UserService;
import com.actvn.Shopee_BE.dto.Request.UserRequest;
import com.actvn.Shopee_BE.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public boolean existByUserName(String userName) {
        return  userRepository.existsByUsername(userName);
    }

    @Override
    public User createNewUser(UserRequest userRequest) {

        String passEn = passwordEncoder.encode(userRequest.getPassword());

        User user = new User(userRequest.getUsername(),
                userRequest.getEmail(),
                passEn);

        System.out.println("Tạo user: " + passEn);

        user.setRoles(userRequest.getRoles());
        return userRepository.save(user);
    }

}
