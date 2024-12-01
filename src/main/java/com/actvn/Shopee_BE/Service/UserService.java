package com.actvn.Shopee_BE.Service;

import com.actvn.Shopee_BE.dto.Request.UserRequest;
import com.actvn.Shopee_BE.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean existByUserName(String userName);
    User createNewUser(UserRequest userRequest);
}

