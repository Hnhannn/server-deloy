package com.yuki.repositoty;

import com.yuki.entity.Book;
import com.yuki.entity.BookBookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookBookTypeDAO extends JpaRepository<BookBookType, Integer> {
    List<BookBookType> findByBook_BookIDAndBookType_BookTypeID(int bookID, int bookTypeID);
    void deleteByBook(Book book);
}
