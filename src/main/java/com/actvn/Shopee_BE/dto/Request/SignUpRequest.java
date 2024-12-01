package com.actvn.Shopee_BE.dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
public class SignUpRequest {
    private String username;

    private String email;

    @Setter
    @Getter
    private Set<String> roles;

    private String password;
}
