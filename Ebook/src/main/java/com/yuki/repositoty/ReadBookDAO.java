package com.yuki.repositoty;

// import com.yuki.entity.Book;
// import com.yuki.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yuki.entity.ReadBook;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReadBookDAO extends JpaRepository<ReadBook, Integer> {

    // Truy vấn kết hợp tìm sách có lượt đọc trong tháng và chi tiết lượt đọc của
    // từng cuốn sách
    @Query("SELECT  r.book.title AS bookTitle, COUNT(r) AS readCount, r.dateRead AS dateRead, r.activityType AS activityType "
            +
            "FROM ReadBook r " +
            "WHERE r.dateRead >= :startDate " +
            "AND r.dateRead < :endDate " +
            "GROUP BY r.book.title, r.dateRead, r.activityType " +
            "ORDER BY readCount DESC, r.dateRead ASC")
    List<Object[]> findBooksWithHighestReadsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<ReadBook> findByUser_UserIDAndBook_BookID(Integer userId, Integer bookId);

    List<ReadBook> findByUser_UserID(Integer userId);

}
