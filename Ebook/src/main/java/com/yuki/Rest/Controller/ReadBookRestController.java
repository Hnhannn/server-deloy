package com.yuki.Rest.Controller;

import com.yuki.dto.ReadBooksDTO;
import com.yuki.entity.ReadBook;
import com.yuki.repositoty.ReadBookDAO;
import com.yuki.service.ReadBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class ReadBookRestController {
    @Autowired
    private ReadBookService readBookService;

    @Autowired
    private ReadBookDAO readBookDAO;

    @GetMapping("/readBook")
    public Iterable<ReadBook> getReadBook() {
        return readBookDAO.findAll();
    }

    @GetMapping("/readBook/user/{userId}/book/{bookId}")
    public List<ReadBook> getReadBookByUserIdAndBookId(@PathVariable Integer userId, @PathVariable Integer bookId) {
        return readBookDAO.findByUser_UserIDAndBook_BookID(userId, bookId);
    }

    @GetMapping("/readBook/user/{userId}")
    public List<ReadBook> getReadBookByUserId(@PathVariable Integer userId) {
        return readBookDAO.findByUser_UserID(userId);
    }

    // API thêm sách vào danh sách đã đọc
    @PostMapping("/readBook")
    public ReadBook addReadBook(@RequestBody ReadBooksDTO readBooksDTO) {
        return readBookService.addReadBook(readBooksDTO);
    }

    // @GetMapping("/top-read/{year}/{month}")
    // public List<Book> getTopReadBooksInMonth(@PathVariable int year,
    // @PathVariable int month) {
    // // Chuyển tháng từ số nguyên thành enum Month
    // Month monthEnum = Month.of(month);
    // return readBookService.getTopReadBooksInMonth(year, monthEnum);
    // }

    // // API để lấy chi tiết lượt đọc của một cuốn sách trong tháng theo năm, tháng
    // @GetMapping("/readBook/details/{bookId}/{year}/{month}")
    // public List<ReadBook> getBookDetailsInMonth(@PathVariable int bookId,
    // @PathVariable int year, @PathVariable int month) {
    // // Trả về danh sách chi tiết sách được đọc trong tháng theo năm
    // return readBookService.getBookDetailsInMonth(bookId, year, month);
    // }

    // API lấy thông tin sách và chi tiết lượt đọc trong tháng
    // API lấy thông tin sách và chi tiết lượt đọc trong tháng
    @GetMapping("/readBook/booksAndDetails/{startDate}/{endDate}")
    public ResponseEntity<List<Object[]>> getBooksWithHighestReads(
            @PathVariable  LocalDateTime startDate,
            @PathVariable LocalDateTime endDate) {

        

        List<Object[]> result = readBookService.getBooksWithHighestReads(startDate, endDate);

        return ResponseEntity.ok(result);
    }
}
