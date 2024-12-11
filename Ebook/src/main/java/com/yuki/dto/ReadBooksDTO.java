package com.yuki.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadBooksDTO {
    private int readId;
    private int bookId;
    private int userId;
    private Float progress;
    private boolean activityType;
    private LocalDateTime dateRead;
}
