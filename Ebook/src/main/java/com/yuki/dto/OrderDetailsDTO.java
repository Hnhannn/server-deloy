package com.yuki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
    private int orderDetailId;
    private int orderId;
    private int bookId;
    private int quantity;
    private Long price;
}
