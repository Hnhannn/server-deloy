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
public class AddressDTO {
    private int addressId;
    private int userId;

    @NotBlank(message = "Địa chỉ cụ thể nhận hàng không được trống")
    @Pattern(regexp = "^[a-zA-Z0-9À-ỹ\\s,.-/]+$", message = "Địa chỉ cụ thể nhận hàng chỉ có số, chữ và các ký tự hợp lệ như dấu - , dấu chấm, dấu phẩy, dấu /")
    private String addressLine;

    @NotNull(message = "Chưa chọn phường/xã")
    @Min(value = 1, message = "Phường/xã không hợp lệ")
    private Integer suburb;

    @NotNull(message = "Chưa chọn quận/huyện")
    @Min(value = 1, message = "Quận/huyện không hợp lệ")
    private Integer district;

    @NotNull(message = "Chưa chọn tỉnh/thành phố")
    @Min(value = 1, message = "Tỉnh/thành phố không hợp lệ")
    private Integer city;

    @NotBlank(message = "Số điện thoại không được trống")
    @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không đúng định dạng")
    private String phoneNumber;
}