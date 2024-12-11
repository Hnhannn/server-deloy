package com.yuki.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsDTO {
    private int reviewId;
    private int rating;
    private String reviewContent;
    private LocalDateTime reviewDate;
    private int userId;
    private int bookId;
}
