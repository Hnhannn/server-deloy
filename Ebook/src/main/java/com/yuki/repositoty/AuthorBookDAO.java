package com.yuki.repositoty;

import java.util.Optional;

import com.yuki.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.AuthorBook;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorBookDAO extends JpaRepository<AuthorBook, Integer> {
    boolean existsByAuthor_AuthorIDAndBook_BookID(Integer author, Integer book);
    void deleteByBook(Book book);
    Optional<AuthorBook> findByAuthor_AuthorIDAndBook_BookID(Integer authorId, Integer bookId);

}