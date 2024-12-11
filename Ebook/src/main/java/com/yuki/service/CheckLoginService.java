package com.yuki.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yuki.jwt.JwtTokenProvider;

@Service
public class CheckLoginService {

    private JwtTokenProvider jwtTokenProvider;

    public CheckLoginService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private static final Logger logger = LoggerFactory.getLogger(CheckLoginService.class);

    public Map<String, Object> checkLoginStatus(String token) {
        Map<String, Object> response = new HashMap<>();

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            response.put("loginStatus", false);
        } else {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            String role = jwtTokenProvider.getRoleFromToken(token); // Lấy vai trò từ token
            String status = jwtTokenProvider.getStatusFromToken(token);
            try {
                response.put("loginStatus", true);
                response.put("username", username);
                response.put("customerImage", "");
                response.put("role", role);
                response.put("status", status);
            } catch (Exception e) {
                logger.error("Exception when get response from token, message: {}" + e.getMessage(), e);
            }
        }
        return response;
    }
}