package com.actvn.Shopee_BE.Security.service;

import com.actvn.Shopee_BE.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails{

    private String id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    public UserDetailsImpl(String id, String email, String username, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().toString()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),  // Sửa lại từ getPassword sang getUsername
                user.getPassword(),
                authorities
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password; // Trả về password thật, không phải chuỗi rỗng
    }

    @Override
    public String getUsername() {
        return username; // Trả về username thật, không phải chuỗi rỗng
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Mặc định return true thay vì gọi super
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Mặc định return true thay vì gọi super
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mặc định return true thay vì gọi super
    }

    @Override
    public boolean isEnabled() {
        return true; // Mặc định return true thay vì gọi super
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
