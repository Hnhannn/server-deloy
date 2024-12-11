package com.yuki.Rest.Controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuki.entity.User;
import com.yuki.repositoty.UserDAO;

@RestController
@RequestMapping("/reset")
public class ForgotPasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Random random = new Random();
    private long expiryTime;
    private int verificationCode;

    public int generateVerificationCode() {
        expiryTime = System.currentTimeMillis() + 60 * 1000; // Update expiry time
        verificationCode = random.nextInt(900000) + 100000; // Generate a random 6-digit code
        return verificationCode;
    }

    // Phương thức kiểm tra mã OTP có còn hiệu lực không
    public boolean isOtpExpired() {
        return System.currentTimeMillis() > expiryTime;
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody OtpRequest request) {
        String email = request.getEmail();
        Optional<User> userOptional = userDAO.findByEmail(email);

        //kiểm tra mail có tồn tại ko
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không tồn tại.");
        } else {
//            String otp = verificationCode;
            int verificationCode = generateVerificationCode();
                        sendEmail(email, verificationCode, userOptional.get().getFullName());
            return ResponseEntity.status(HttpStatus.OK).body("Mã OTP đã được gửi đến email của bạn " + verificationCode);
        }
    }


    // Gửi email chứa mã OTP
    private void sendEmail(String to, int otp, String fullName) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Mã OTP đặt lại mật khẩu");
            helper.setText(buildEmailContent(otp, fullName), true); // true để gửi HTML content
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
        }
    }

    private String buildEmailContent(int otp, String fullName) {
        String body = """
        <html>
               <head>
                 <meta charset="UTF-8">
                 <meta name="viewport" content="width=device-width, initial-scale=1.0">
                 <title>Email OTP</title>
                 <style>
                   body {
                     margin: 0;
                     padding: 0;
                     background-color: #f4f4f7;
                     font-family: 'Helvetica Neue', Arial, sans-serif;
                     color: #51545E;
                   }
                   a {
                     text-decoration: none;
                     color: rgb(14, 159, 110);
                   }
                   a:hover {
                     text-decoration: underline;
                   }
                   .email-wrapper {
                     width: 100%;
                     background-color: #f4f4f7;
                     padding: 30px 0;
                   }
                   .email-content {
                     max-width: 600px;
                     margin: 0 auto;
                     background-color: #ffffff;
                     border-radius: 8px;
                     overflow: hidden;
                     box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
                   }
                   .email-header {
                     background-color: rgb(14, 159, 110);
                     color: #ffffff;
                     text-align: center;
                     padding: 40px 20px;
                   }
                   .email-header h1 {
                     font-size: 26px;
                     font-weight: 700;
                     margin: 0;
                   }
                   .email-body {
                     padding: 40px 30px;
                     font-size: 16px;
                     line-height: 1.8;
                   }
                   .otp-code {
                     display: block;
                     margin: 30px auto;
                     text-align: center;
                     font-size: 36px;
                     font-weight: bold;
                     color: rgb(14, 159, 110);
                     background-color: #f9f9fb;
                     padding: 15px 20px;
                     border-radius: 8px;
                     letter-spacing: 4px;
                     width: fit-content;
                   }
                   .email-footer {
                     text-align: center;
                     padding: 20px;
                     background-color: #f9f9fb;
                     font-size: 14px;
                     color: #6B7280;
                   }
                   .email-footer a {
                     color: rgb(14, 159, 110);
                   }
                 </style>
               </head>
               <body>
                 <div class="email-wrapper">
                   <div class="email-content">
                     <!-- Header -->
                     <div class="email-header">
                       <h1>Xác Minh Tài Khoản</h1>
                     </div>
                     <!-- Body -->
                     <div class="email-body">
                       <p>Xin chào <strong>FULL_NAME</strong>,</p>
                       <p>Chúng tôi đã nhận được yêu cầu truy cập tài khoản của bạn. Vui lòng sử dụng mã OTP dưới đây để hoàn tất quá trình:</p>
                       <div class="otp-code">OTP_CODE</div>
                       <p>Mã OTP có hiệu lực trong <strong>1 phút</strong>. Vì lý do bảo mật, vui lòng không chia sẻ mã này với bất kỳ ai.</p>
                       <p>Nếu bạn không yêu cầu truy cập, vui lòng <a href="mailto:support@example.com">liên hệ hỗ trợ</a> ngay lập tức.</p>
                       <p style="margin-top: 30px;">Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>
                     </div>
                     <!-- Footer -->
                     <div class="email-footer">
                       <p>&copy; 2024 Công Ty Của Bạn. Mọi quyền được bảo lưu.</p>
                       <p>Cần hỗ trợ? <a href="mailto:support@example.com">support@example.com</a></p>
                     </div>
                   </div>
                 </div>
               </body>
               </html>
        """;

        body = body.replace("OTP_CODE", String.valueOf(otp));
        body = body.replace("FULL_NAME", fullName);
        return body;
    }




    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtpAndResetPassword(@RequestBody OtpRequest request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String otpCode = request.getOtpCode();

        Optional<User> userOptional = userDAO.findByEmail(email);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không tồn tại.");
        }

//        int verificationCode = generateVerificationCode();
        String Otp = String.valueOf(verificationCode);
        System.out.printf("OTP: %s\n", Otp);
        if (isOtpExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP đã hết hạn.");
        }
        if (otpCode.equals(Otp)) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userDAO.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("Mật khẩu đã được thay đổi thành công.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP không hợp lệ hoặc đã hết hạn.");
        }
    }




    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OtpRequest {
        private String email;
        private String otpCode;
        private String newPassword;
    }
}