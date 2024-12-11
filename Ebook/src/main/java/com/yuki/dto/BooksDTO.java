package com.yuki.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksDTO {
    private int bookId;
    private String title;
    private String bookImage;
    private String description;
    private int publisherId;
    private boolean bookStatus;
    private int price;
    private LocalDateTime postDate;
    // Danh sách chapters
    private List<BookChaptersDTO> chapters;
    // Danh sách authors
    private List<AuthorsDTO> authors;
    // Danh sách categories
    private List<CategoriesDTO> categories;
    // Danh sách bookTypes
    private List<BookTypesDTO> bookTypes;

}
