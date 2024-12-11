package com.yuki.jwt;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private String encodedSecretKey;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    private static final String SCOPE = "scope";
    private static final String STATUS = "status";

    @PostConstruct
    public void init() {
        this.encodedSecretKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("ExpiredJwt Exception {}", ex.getMessage());
        } catch (JwtException ex) {
            logger.error("Jwt Exception {}", ex.getMessage());
        }
        return false;
    }

    public String generateToken(Authentication authentication, String scope, String status) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim(SCOPE, scope).claim(STATUS, status)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, encodedSecretKey)
                .compact();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(encodedSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(SCOPE, String.class);
    }

    public String getStatusFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(encodedSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(STATUS, String.class);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(encodedSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(encodedSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpirationTime(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().getTime() - new Date().getTime();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(encodedSecretKey)
                .parseClaimsJws(token)
                .getBody();

        String scope = claims.get(SCOPE, String.class);
        String status = claims.get(STATUS, String.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(scope));

        logger.info("User status: {}", status);

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String generateTokenGoogle(String email, String scope, String status) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim(SCOPE, scope).claim(STATUS, status)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, encodedSecretKey)
                .compact();
    }

}