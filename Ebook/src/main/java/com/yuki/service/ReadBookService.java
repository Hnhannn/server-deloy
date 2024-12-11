package com.yuki.service;

import com.yuki.dto.ReadBooksDTO;
import com.yuki.entity.ReadBook;
import com.yuki.repositoty.BookChapterDAO;
import com.yuki.repositoty.BookDAO;
import com.yuki.repositoty.ReadBookDAO;
import com.yuki.repositoty.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReadBookService {
    @Autowired
    private ReadBookDAO readBookDAO;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BookChapterDAO bookChapterDAO;

    // Thêm sách vào danh sách đã đọc
    public ReadBook addReadBook(ReadBooksDTO readBooksDTO) {
        // Tìm kiếm ReadBook đã tồn tại
        List<ReadBook> existingReadBooks = readBookDAO.findByUser_UserIDAndBook_BookID(
                readBooksDTO.getUserId(), readBooksDTO.getBookId());

        ReadBook readBook;
        if (!existingReadBooks.isEmpty()) {
            // Nếu đã tồn tại, lấy ReadBook đầu tiên và cập nhật dateRead và progress nếu
            // cần
            readBook = existingReadBooks.get(0);
            readBook.setDateRead(LocalDateTime.now());
            readBook.setProgress(readBooksDTO.getProgress());
            readBook.setActivityType(readBooksDTO.isActivityType());
        } else {
            // Nếu chưa tồn tại, tạo mới
            readBook = new ReadBook();
            readBook.setBook(bookDAO.findById(readBooksDTO.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + readBooksDTO.getBookId())));
            readBook.setUser(userDAO.findById(readBooksDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + readBooksDTO.getUserId())));
            readBook.setDateRead(LocalDateTime.now());
            readBook.setProgress(readBooksDTO.getProgress());
            readBook.setActivityType(readBooksDTO.isActivityType());
        }

        return readBookDAO.save(readBook);
    }

    // Phương thức lấy thông tin sách có lượt đọc cao nhất trong khoảng thời gian từ startDate đến endDate
    public List<Object[]> getBooksWithHighestReads(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            LocalDateTime now = LocalDateTime.now();
            startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0); // Ngày đầu tháng
            endDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999); // Ngày cuối tháng
        }
        return readBookDAO.findBooksWithHighestReadsByDateRange(startDate, endDate);
    }
}
