package com.yuki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionsDTO {
    private int userSubscriptionId;
    private int userId;
    private int planId;
    private LocalDateTime subscriptionDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

}
