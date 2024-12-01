package com.actvn.Shopee_BE.dto.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    private String userId;

    private String jwtToken;

    private String userName;

    private List<String> roles;

    public UserInfoResponse(String userId, String userName, List<String> roles) {
        this.userId = userId;
        this.userName = userName;
        this.roles = roles;
    }

}
