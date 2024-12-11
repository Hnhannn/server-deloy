package com.yuki.dto;

import com.yuki.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {
    private int orderId;
    private int userId;
    private int paymentMethodId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Long totalAmount;
}
