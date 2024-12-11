package com.yuki.email;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yuki.entity.User;
import com.yuki.enums.Status;
import com.yuki.repositoty.UserDAO;

@RestController
@RequestMapping("/api/otp")
public class OTPController {
    private static final Logger logger = LoggerFactory.getLogger(OTPController.class);

    private OTPService otpService;
    private UserDAO userDAO;

    public OTPController(OTPService otpService, UserDAO userDAO) {
        this.otpService = otpService;
        this.userDAO = userDAO;
    }

    /**
     * API để gửi OTP qua email
     * 
     * @param email Địa chỉ email người dùng
     * @return Thông báo thành công hoặc lỗi
     */
    
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            otpService.generateOTP(email);
            return ResponseEntity.ok("OTP has been sent to " + email);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP to email");
        }
    }

    @PostMapping("/verify")
    @JsonProperty("otpRequest")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest otpRequest) {
        logger.info("Received OTP request: {}", otpRequest);
        try {
            boolean isValid = otpService.verifyOTP(otpRequest.getEmail(), otpRequest.getOtpCode());
            if (!isValid) {
                logger.warn("Invalid or expired OTP for email: {}", otpRequest.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
            }
            Optional<User> optionalUser = userDAO.findByEmail(otpRequest.getEmail());
            if (optionalUser.isEmpty()) {
                logger.warn("User not found for email: {}", otpRequest.getEmail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
            User user = optionalUser.get();
            user.setStatus(Status.ACTIVE);
            userDAO.save(user);
            return ResponseEntity.ok("OTP verified successfully and user status is now ACTIVE.");

        } catch (Exception e) {
            logger.error("Error verifying OTP", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during verification.");
        }
    }

}