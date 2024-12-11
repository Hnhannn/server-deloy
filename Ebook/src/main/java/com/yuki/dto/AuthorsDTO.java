package com.yuki.dto;

import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorsDTO {
    private int authorId;
    @NotBlank(message = "Tên tác giả không được để trống")
    ///@Pattern(regexp = "^[A-Za-zÀ-ỹ0-9s]+$", message = "Tên tác giả chỉ được chứa chữ cái, số và khoảng trắng ,không được chứa ký tự đặc biệt.")
    private String authorName;
    private boolean status;



}
