package com.yuki.Rest.Controller;

import com.yuki.dto.BookCategoriesDTO;
import com.yuki.entity.BookCategory;
import com.yuki.service.BookCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class BookCategoriesController {
    @Autowired
    private BookCategoriesService bookCategoriesService;

    @PostMapping("/bookCategories")
    public List<BookCategory> addBookCategori(@RequestBody BookCategoriesDTO bookCategoryDTO) {
        // Gọi tới service để xử lý thêm thể loại sách
        return bookCategoriesService.addBookCate(bookCategoryDTO);
    }
}
