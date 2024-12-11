package com.yuki.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDTO {
    private int categoryId;
    @NotBlank(message = "Tên thể loại không được để trống.")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên thể loại chỉ được chứa chữ cái và số, không được chứa ký tự đặc biệt.")
    private String categoryName;
    private boolean status;
}
