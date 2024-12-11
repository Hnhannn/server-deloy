package com.yuki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookChaptersDTO {
    private int chapterId;
    private int bookId;
    private String chapterTitle;
    private String chapterContent;
    private String audioLink;

}
