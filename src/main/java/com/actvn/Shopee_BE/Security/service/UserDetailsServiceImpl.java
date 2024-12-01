package com.actvn.Shopee_BE.Security.service;

import com.actvn.Shopee_BE.Repository.UserRepository;
import com.actvn.Shopee_BE.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("Lỗi ở detailsService"));

        System.out.println("User found: " + user.getUsername()+" password: " + user.getPassword());

        return UserDetailsImpl.build(user);
    }
}
