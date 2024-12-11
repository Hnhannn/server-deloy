package com.yuki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoriesDTO {
    private int bookCategoryId;
    private List<Integer> categoryIds;
    private int bookId;

}
