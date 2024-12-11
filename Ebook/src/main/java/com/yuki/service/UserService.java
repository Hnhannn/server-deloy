package com.yuki.service;

import java.time.LocalDate;
import java.util.Optional;

import com.yuki.dto.ChangePasswordDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.yuki.entity.User;
import com.yuki.repositoty.UserDAO;

@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = userDAO.findByUsername(username);

        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        return BCrypt.checkpw(password, user.getPassword());
    }

    @Autowired
    private JavaMailSender emailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã OTP Đặt lại mật khẩu");
        message.setText("Mã OTP của bạn là: " + otp);
        emailSender.send(message);
    }

    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(changePasswordDTO.getUsername(), changePasswordDTO.getOldPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<User> userOptional = userDAO.findByUsername(changePasswordDTO.getUsername());
            if (userOptional.isEmpty()) {
                return false;
            }
            User user = userOptional.get();
            user.setPassword(BCrypt.hashpw(changePasswordDTO.getNewPassword(), BCrypt.gensalt()));
            userDAO.save(user);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    public User updateUserDetails(int userId, boolean gender, String fullName, String userImage, LocalDate birthday) {
        User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setGender(gender);
        user.setFullName(fullName);
        user.setUserImage(userImage);
        user.setBirthday(birthday);
        return userDAO.save(user);
    }
}