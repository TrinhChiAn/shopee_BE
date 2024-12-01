package com.actvn.Shopee_BE.Controller;

import com.actvn.Shopee_BE.Security.jwt.JwtUtils;
import com.actvn.Shopee_BE.Security.service.UserDetailsImpl;
import com.actvn.Shopee_BE.Service.RoleService;
import com.actvn.Shopee_BE.Service.UserService;
import com.actvn.Shopee_BE.dto.Request.LoginRequest;
import com.actvn.Shopee_BE.dto.Request.SignUpRequest;
import com.actvn.Shopee_BE.dto.Request.UserRequest;
import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import com.actvn.Shopee_BE.dto.Response.UserInfoResponse;
import com.actvn.Shopee_BE.entity.AppRole;
import com.actvn.Shopee_BE.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private  AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    private PasswordEncoder encoder;


    private final RoleService roleService;

    @PostMapping("/signIn")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try {
            String rawPassword = "admin"; // Mật khẩu người dùng nhập vào
            String encodedPasswordFromDb = encoder.encode("admin"); // Mật khẩu lưu trong cơ sở dữ liệu

            boolean matches = encoder.matches(rawPassword, encodedPasswordFromDb);
            System.out.println("Password matches: " + matches);

            authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status", false);
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND)
                    .body(map);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority)
                .toList();
        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);
        UserInfoResponse response = new UserInfoResponse(
                ( userDetails).getId(),
                cookie.toString(),
                userDetails.getUsername(),
                roles
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PostMapping("/signUp")
    public ApiResponse<Object> registerUser(@RequestBody SignUpRequest sign){


        UserRequest newUser = new UserRequest(
                sign.getUsername(),
                sign.getEmail(),
                sign.getPassword()
        );

        Set<String> strRoles = sign.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null){
            Role role = roleService.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found"));

            roles.add(role);
        }
        else {
            for (String role: strRoles){
                switch (role){
                    case "ADMIN":
                        Role adminRole = roleService.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() ->new RuntimeException("Role is not found"));

                        roles.add(adminRole);
                        break;
                    case "SELLER":
                        Role sellerRole = roleService.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() ->new RuntimeException("Role is not found"));

                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleService.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() ->new RuntimeException("Role is not ROLE_ADMINfound"));

                        roles.add(userRole);
                        break;
                }
            }
        }

        newUser.setRoles(roles);


        return ApiResponse.builder()
                .status(HttpStatus.CREATED)
                .result(userService.createNewUser(newUser))
                .build();

    }

    @PostMapping("/signOut")
    public ResponseEntity<String> signOutUser(){
        ResponseCookie cleanJwtCookie = jwtUtils.generateCleanCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString())
                .body("oke");
    }

    @GetMapping("/myInfo")
    public ResponseEntity<ApiResponse<Object>> getUserDetail(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles
        );

        return ResponseEntity.ok().body(ApiResponse.builder().result(response).build());
    }

    @GetMapping("/username")
    public String currentUser(Authentication authentication){
        if(authentication!=null){
            return authentication.getName();
        }
        return null;
    }
}
