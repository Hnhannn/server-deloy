package com.yuki.dto;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthGoogleDto {
    private String email;
    private boolean emailVerified;
    private String phoneNumber;
    private String photoURL;
    private String displayName;


}
