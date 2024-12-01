package com.actvn.Shopee_BE.Security.jwt;

import com.actvn.Shopee_BE.Security.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookieName;

    public String generateJwtTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        Date expiration = new Date((new Date().getTime() + jwtExpirationMs));
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(key())
                .compact();

    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromJwtToken(String jwtToken){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerTokens = request.getHeader("Authorization");
        logger.info("Authorization: Header: {}", bearerTokens);
        if(bearerTokens!=null && bearerTokens.startsWith("Bearer "))
            return bearerTokens.substring(7);
        return null;
    }

    public boolean validateJwtToken(String authToken){
        logger.info("Validate jwt token");
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);

            return true;
        }catch (MalformedJwtException exception){
            logger.error("Invalid JWT token: {}", exception.getMessage());
        }catch (ExpiredJwtException exception){
            logger.error("JWT token is expired: {}", exception.getMessage());
        }catch (UnsupportedJwtException exception){
            logger.error("unsupported: {}", exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            logger.error("JWT claims String is empty: {}", exception.getMessage());
        }
        return false;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        String jwt = generateJwtTokenFromUsername(userDetails);
        return ResponseCookie.from(jwtCookieName, jwt)
                .path("api")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();
    }

    public ResponseCookie generateCleanCookie() {
        return ResponseCookie.from(jwtCookieName, null)
                .path("api")
                .build();
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

}
