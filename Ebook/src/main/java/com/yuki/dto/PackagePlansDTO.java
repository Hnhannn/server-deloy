package com.yuki.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackagePlansDTO {
    private int planId;

    @NotBlank(message = "Tên gói cước không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9 \\p{L}]+$", message = "Tên gói cước chỉ được chứa chữ cái, số và khoảng trắng")
    private String planName;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 1, message = "Giá phải là số dương và lớn hơn 0")
    private Long price;

    @NotNull(message = "Thời gian không được để trống")
    @Min(value = 1, message = "Thời gian phải là số dương và lớn hơn 0")
    private Integer duration;

    private boolean status;
}
