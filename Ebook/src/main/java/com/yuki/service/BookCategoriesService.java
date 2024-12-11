package com.yuki.service;

import com.yuki.dto.BookCategoriesDTO;
import com.yuki.entity.Book;
import com.yuki.entity.BookCategory;
import com.yuki.entity.Category;
import com.yuki.repositoty.BookCategoryDAO;
import com.yuki.repositoty.BookDAO;
import com.yuki.repositoty.CategoryDAO;
// import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookCategoriesService {

    @Autowired
    private BookCategoryDAO bookCategoryDAO;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    public List<BookCategory> addBookCate(BookCategoriesDTO bookCategoriesDTO){
        Book book = bookDAO.findById(bookCategoriesDTO.getBookId()).orElse(null);
        List<BookCategory> bookCategories = new ArrayList<>();
        for (int categoryId : bookCategoriesDTO.getCategoryIds()) {
            // Lấy thông tin thể loại từ categoryId
            Category category = categoryDAO.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            // Tạo đối tượng BookCategory mới
            BookCategory bookCategory = new BookCategory();
            bookCategory.setBook(book);
            bookCategory.setCategory(category);

            // Thêm vào danh sách
            bookCategories.add(bookCategory);
        }
        return bookCategoryDAO.saveAll(bookCategories);
    }
}
