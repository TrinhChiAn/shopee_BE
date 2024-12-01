package com.actvn.Shopee_BE.ultil;

import com.actvn.Shopee_BE.Repository.UserRepository;
import com.actvn.Shopee_BE.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    @Autowired
    private UserRepository userRepository;

    public String getEmailLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("user not found: " + authentication.getName())
        );
        return user.getEmail();
    }

    public User getUserLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("user not found: " + authentication.getName())
        );
    }
}
