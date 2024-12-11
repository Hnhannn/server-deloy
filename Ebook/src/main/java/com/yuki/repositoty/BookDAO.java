package com.yuki.repositoty;

// import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yuki.entity.Book;
import com.yuki.entity.ReadBook;

@Repository
public interface BookDAO extends JpaRepository<Book, Integer> {
//    List<Book> findByPrice(Double price);
List<Book> findByPriceGreaterThanAndBookStatusTrue(int price);
    Optional<Book> findByBookIDAndBookStatus(int id, boolean bookStatus);
    List<Book> findByBookBookTypes_AccessTypeAndBookStatus(String accessType, boolean bookStatus);
    List<Book> findByBookBookTypes_AccessTypeAndPriceAndBookStatus(String accessType, int price, boolean bookStatus);
    Page<Book> findAllByBookStatusTrueOrderByPostDateDesc(Pageable pageable);
    @Query("SELECT b FROM Book b LEFT JOIN b.authorBooks ab LEFT JOIN ab.author a WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.authorName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> findByTitleOrAuthorNameContaining(@Param("keyword") String keyword);
    List<Book> findByBookIDAndBookBookTypes_BookBookTypeID(int bookID, int bookBookTypeID);
    List<Book> findByBookIDAndBookBookTypes_BookType_BookTypeID(int bookID, int bookBookTypeID);
    List<Book> findByBookBookTypes_bookType_BookTypeIDAndBookStatus(int bookTypeID, boolean bookStatus);
    @Query("SELECT b FROM Book b WHERE b.bookID IN :bookIds AND b.bookStatus = true")
    List<Book> findBooksByIdsAndStatusTrue(@Param("bookIds") List<Integer> bookIds);

}