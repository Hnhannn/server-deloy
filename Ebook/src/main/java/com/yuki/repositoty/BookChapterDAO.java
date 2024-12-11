package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.BookChapter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookChapterDAO extends JpaRepository<BookChapter, Integer> {
        List<BookChapter> findByBook_bookID(int bookId);
}