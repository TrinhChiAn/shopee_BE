package com.actvn.Shopee_BE.Controller;

import com.actvn.Shopee_BE.Security.jwt.JwtUtils;
import com.actvn.Shopee_BE.dto.Request.LoginRequest;
import com.actvn.Shopee_BE.dto.Response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class SpringSecurityDemoController {

    private static final Logger logger = LoggerFactory.getLogger(SpringSecurityDemoController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Security!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String sayHelloUser() {
        return "Hello, Spring Security!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String sayHelloAdmin() {
        return "Hello, Spring Security!";
    }

    @PostMapping("/signIn")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND)
                    .body(map);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority)
                .toList();
        LoginResponse response = new LoginResponse(userDetails.getUsername(),
                jwtToken, roles);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
}
