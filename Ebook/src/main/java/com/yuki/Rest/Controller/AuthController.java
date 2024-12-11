package com.yuki.Rest.Controller;


import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

// import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthException;
import com.yuki.dto.AuthGoogleDto;
import com.yuki.entity.User;
import com.yuki.jwt.JwtResponse;
import com.yuki.jwt.JwtTokenProvider;
import com.yuki.repositoty.UserDAO;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Cho phép các yêu cầu từ mọi nguồn
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDAO userDAO;
    public String extractUsernameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        return email.split("@")[0];
    }
    @PostMapping("/loginGoogle")
    public ResponseEntity<?> login(@RequestBody AuthGoogleDto authGoogleDto) throws FirebaseAuthException {
        // Trích xuất thông tin từ FirebaseToken
        String username = extractUsernameFromEmail(authGoogleDto.getEmail());
        String email = authGoogleDto.getEmail();
        String fullName = authGoogleDto.getDisplayName(); // Lấy tên đầy đủ của người dùng
        String userImage = authGoogleDto.getPhotoURL();
        String role = "CLIENT";
        String status = authGoogleDto.isEmailVerified() ? "ACTIVE" : "UNVERIFIED";

        String phoneNumber = authGoogleDto.getPhoneNumber();


        if (Boolean.TRUE.equals(userDAO.existsByEmail(email))) {
            String jwtToken = jwtTokenProvider.generateTokenGoogle(username, role, status);
            JwtResponse jwtResponse = new JwtResponse(jwtToken, role, status);
            return ResponseEntity.ok(jwtResponse);
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName); 
        user.setUserImage(userImage); 
        user.setRole(false);
        user.setStatus(status);
        user.setPhoneNumber(phoneNumber);
        user.setUsername(username);
        userDAO.save(user);

        String jwtToken = jwtTokenProvider.generateTokenGoogle(username, role, status);

        JwtResponse jwtResponse = new JwtResponse(jwtToken, role, status);
        return ResponseEntity.ok(jwtResponse);
    }

}