package com.yuki.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {
    private int commentId;
    private LocalDateTime commentTime;
    private int userId;
    private int bookId;
}
