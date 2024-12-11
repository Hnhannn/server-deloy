package com.yuki.email;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.yuki.entity.User;
import com.yuki.repositoty.UserDAO;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class OTPService {
  private static final Logger logger = LoggerFactory.getLogger(OTPService.class);
  private final UserDAO userDAO;
  private final JavaMailSender mailSender;

  public OTPService(JavaMailSender mailSender, UserDAO userDAO) {
    this.mailSender = mailSender;
    this.userDAO = userDAO;
  }

  private static final int OTP_LENGTH = 6;
  private static final long OTP_EXPIRY_TIME_IN_MINUTES = 2;

  private final Map<String, OtpCode> otpStorage = new ConcurrentHashMap<>();

  public String generateOTP(String email) {
    String otpCode = generateRandomOTP();
    LocalDateTime expiryTime = (LocalDateTime.now().plusMinutes(OTP_EXPIRY_TIME_IN_MINUTES));

    otpStorage.put(email, new OtpCode(otpCode, expiryTime));
    Optional<User> userOptional = userDAO.findByEmail(email);
    String fullName = userOptional.map(user -> 
    user.getFullName() != null && !user.getFullName().isEmpty() 
        ? user.getFullName() 
        : user.getUsername()
).orElse(email);     System.out.printf("fullName",fullName);
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(email);
      helper.setSubject("Mã OTP xác thực");
      int otp = Integer.parseInt(otpCode);
      helper.setText(buildEmailContent(otp, fullName), true);
      mailSender.send(message);
      logger.info("OTP generate success at OTPServive");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("error generate message: {}", e.getMessage());
    }
    logger.info("otpCode: {} ", otpCode);
    return otpCode;
  }

  private String generateRandomOTP() {
    StringBuilder otp = new StringBuilder();
    for (int i = 0; i < OTP_LENGTH; i++) {
      otp.append(ThreadLocalRandom.current().nextInt(10));
    }
    return otp.toString();
  }

  public boolean verifyOTP(String email, String inputOtp) {

    if (email == null || email.isEmpty()) {
      logger.warn("Empty email provided for OTP verification.");
      return false;
    }
    if (inputOtp == null || inputOtp.isEmpty()) {
      logger.warn("Empty OTP entered for verification.");
      return false;
    }
    OtpCode otp = otpStorage.get(email);
    if (otp == null) {
      logger.info("No OTP found for email: {}", email);
      return false;
    }
    LocalDateTime expiryWithBuffer = otp.getExpiryTime().minusMinutes(1);
    if (expiryWithBuffer.isBefore(LocalDateTime.now())) {
      logger.info("OTP expired for email: {}", email);
      otpStorage.remove(email);
      return false;
    }
    if (otp.getCode().equals(inputOtp)) {
      logger.info("OTP verified successfully for email: {}", email);
      otpStorage.remove(email);
      return true;
    }

    logger.info("Invalid OTP entered for email: {}", email);
    return false;
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

}