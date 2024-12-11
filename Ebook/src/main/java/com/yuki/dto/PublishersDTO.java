package com.yuki.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishersDTO {
    private int publisherId;
    @NotBlank(message = "Tên nhà xuất bản không được để trống.")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên nhà xuất bản chỉ được chứa chữ cái và số, không được chứa ký tự đặc biệt.")
    private String publisherName;
    private boolean status;
}
