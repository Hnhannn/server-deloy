package com.yuki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorBooksDTO {
    private int authorBookId;
    private int authorId;
    private int bookId;

}
