package com.yuki.Rest.Controller;

// import com.yuki.entity.Book;
import com.yuki.entity.BookChapter;
import com.yuki.repositoty.BookChapterDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/rest")
public class BookChapterRestController {
    @Autowired
    private BookChapterDAO bookChapterDAO;

    @GetMapping("/chapter/{id}")
    public ResponseEntity<List<BookChapter>> getChaptersByBookId(@PathVariable int id) {
        List<BookChapter> chapters = bookChapterDAO.findByBook_bookID(id);
        if (!chapters.isEmpty()) {
            return ResponseEntity.ok(chapters);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
