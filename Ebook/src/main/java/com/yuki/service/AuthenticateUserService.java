package com.yuki.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yuki.entity.User;
import com.yuki.repositoty.UserDAO;

@Service
public class AuthenticateUserService {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    public AuthenticateUserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = userDAO.findByUsername(username);

        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        return passwordEncoder.matches(password, user.getPassword());
    }
}
