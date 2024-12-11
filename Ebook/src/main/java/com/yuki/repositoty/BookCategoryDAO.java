package com.yuki.repositoty;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.Book;
import com.yuki.entity.BookCategory;
import com.yuki.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryDAO extends JpaRepository<BookCategory, Integer> {
    boolean existsByCategoryAndBook(Category category, Book book);
    void deleteByBook(Book book);
    Optional<BookCategory> findByCategory_CategoryIDAndBook_BookID(Integer categoryid, Integer bookid);
}