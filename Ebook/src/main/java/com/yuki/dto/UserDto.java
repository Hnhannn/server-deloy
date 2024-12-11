package com.yuki.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "Vui lòng nhập họ và tên")
    @Pattern(regexp = "^([A-ZÀ-Ỹ][a-zà-ỹ]*(?:[- ][A-ZÀ-Ỹ][a-zà-ỹ]*){0,5})$", message = "Tên chỉ là các chữ cái, chữ cái đầu tiên được viết hoa. (Ví dụ: Nguyễn Văn A)")
    private String fullName;
    @NotBlank(message = "Vui lòng nhập username")
    private String username;
    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    private String email;
    private LocalDate birthday; // Add this line
    private String password;
    private boolean gender;
    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(regexp = "^0[35789]\\d{8}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;
    private boolean role;
    private String status;
    private String userImage;
}
