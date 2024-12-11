package com.yuki.Rest.Controller;

import com.yuki.dto.BooksDTO;
import com.yuki.entity.Book;
import com.yuki.entity.BookBookType;
import com.yuki.entity.BookType;
import com.yuki.entity.ReadBook;
import com.yuki.repositoty.BookDAO;
import com.yuki.repositoty.ReadBookDAO;
import com.yuki.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class BookRestController {
    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private ReadBookDAO readBookDAO;

    @Autowired
    private BooksService booksService;

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookDAO.findAll();
    }

    @GetMapping("/books/price/greaterThanZero")
    public List<Book> getBooksPriceGreaterThanZero() {
        return bookDAO.findByPriceGreaterThanAndBookStatusTrue(0);
    }

    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookDAO.findByTitleOrAuthorNameContaining(keyword);
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody BooksDTO booksDTO) {
        return booksService.bookCreate(booksDTO);
    }

    @PutMapping("/books/{bookId}")
    public Book updateBook(@PathVariable int bookId,@RequestBody BooksDTO booksDTO) {
        return booksService.updateBook(bookId , booksDTO);
    }

    @GetMapping("/books/free")
    public List<Book> getFreeBook() {
        return bookDAO.findByBookBookTypes_AccessTypeAndPriceAndBookStatus("Miễn phí" ,0, true);
    }

    @GetMapping("/books/vip")
    public List<Book> getVIPBook() {
        return bookDAO.findByBookBookTypes_AccessTypeAndPriceAndBookStatus("Hội viên" ,0, true);
    }

    @GetMapping("/books/new")
    public List<Book> getNewBooks(@RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Book> books = new ArrayList<>(bookDAO.findAllByBookStatusTrueOrderByPostDateDesc(pageable).getContent());
        return books.subList(0, Math.min(limit, books.size()));
    }

    @GetMapping("/books/recommendations/{userId}")
    public List<Book> getPersonalizedBooks(@PathVariable int userId) {
        // Fetch the user's reading history
        List<ReadBook> readBooks = readBookDAO.findByUser_UserID(userId);
        List<Book> recommendedBooks;

        if (readBooks.isEmpty() || readBooks.size() < 5) {
            // Fetch a random selection of 10 books
            Pageable pageable = PageRequest.of(0, 10);
            recommendedBooks = new ArrayList<>(bookDAO.findAllByBookStatusTrueOrderByPostDateDesc(pageable).getContent());
            Collections.shuffle(recommendedBooks);
        } else {
            // Fetch books based on the reading history
            List<Integer> bookIds = readBooks.stream()
                    .map(readBook -> readBook.getBook().getBookID())
                    .collect(Collectors.toList());
            recommendedBooks = bookDAO.findBooksByIdsAndStatusTrue(bookIds);
        }

        // Return the recommended books
        return recommendedBooks;
    }

    @GetMapping("/books/random")
    public List<Book> getRandomBooks() {
        List<Book> allBooks = bookDAO.findAll();
        Collections.shuffle(allBooks);
        return allBooks.subList(0, Math.min(10, allBooks.size()));
    }

	  // API lấy dữ liệu UserSubscription theo ID
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getUserSubscriptionById(@PathVariable int id) {
        Optional<Book> book = bookDAO.findByBookIDAndBookStatus(id, true);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/books/{bookID}/booktype/{bookTypeID}")
    public List<Book> getBookByBookIDAndBookTypeID(@PathVariable int bookID, @PathVariable int bookTypeID) {
        return bookDAO.findByBookIDAndBookBookTypes_BookBookTypeID(bookID, bookTypeID);
    }

    @GetMapping("/books/booktype/{bookTypeID}")
    public ResponseEntity<List<Book>> getBooksWithSpecificBookType(@PathVariable int bookTypeID) {
        List<Book> books = bookDAO.findByBookBookTypes_bookType_BookTypeIDAndBookStatus(bookTypeID, true);
        if (!books.isEmpty()) {
            for (Book book : books) {
                book.setBookBookTypes(book.getBookBookTypes().stream()
                        .filter(bookBookType -> bookBookType.getBookTypeID() == bookTypeID)
                        .collect(Collectors.toList()));
            }
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

//    @GetMapping("/books/booktype/{bookTypeID}")
//    public List<Book> getBookByBookTypeID(@PathVariable int bookTypeID) {
//        return bookDAO.findByBookBookTypes_bookType_BookTypeIDAndBookStatus(bookTypeID, true);
//    }

    @GetMapping("/books/accessType/{accessType}")
    public List<Book> getBookByAccessType(@PathVariable String accessType) {
        return bookDAO.findByBookBookTypes_AccessTypeAndBookStatus(accessType, true);
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable int bookId) {
        boolean isDeleted = booksService.deleteBook(bookId);
        if (isDeleted) {
            return ResponseEntity.ok("Book deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

}
