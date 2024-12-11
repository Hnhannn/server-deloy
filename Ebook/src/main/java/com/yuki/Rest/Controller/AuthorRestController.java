package com.yuki.Rest.Controller;

import com.yuki.dto.AuthorsDTO;
import com.yuki.entity.Author;
import com.yuki.repositoty.AuthorDAO;
import com.yuki.service.AuthorsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class AuthorRestController {
    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private AuthorsService authorsService;

    //API get All dữ liệu Author with status = true
    @GetMapping("/author")
    public List<Author> getAllActiveAuthors() {
        return authorDAO.findByStatusTrue();
    }

    @GetMapping("/author/{id}")
    public Author author(@PathVariable int id){
        Optional<Author> author = authorDAO.findById(id);
        if (author.isPresent()) {
            return author.get();
        }
        return null;
    }
    //API Create Publishers
    @PostMapping("/author")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorsDTO authors) {
        // Kiểm tra nếu name của author đã tồn tại
        if (Boolean.TRUE.equals(authorDAO.existsByAuthorName(authors.getAuthorName()))) {
            return new ResponseEntity<>("Tên tác giả đã được sử dụng cho " + authors.getAuthorName(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            // Assuming you have a method in your service to create an author
            Author createdAuthor = authorsService.createAuthor(authors);
            return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi thêm tác giả.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //API Update
    //API Update
    @PutMapping("/author/{authorID}")
    public ResponseEntity<?> updateAuthor(@PathVariable int authorID, @Valid @RequestBody AuthorsDTO authors) {

        if (Boolean.TRUE.equals(authorDAO.existsByAuthorName(authors.getAuthorName()))) {
            return new ResponseEntity<>("Tên tác giả đã được sử dụng cho " + authors.getAuthorName(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Author updatedAuthor = authorsService.AuthorUpdate(authorID, authors);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật tác giả.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


        //API Delete
        // Update the deleteAuthor method in AuthorRestController
        @DeleteMapping("/author/{authorID}")
        public ResponseEntity<?> deleteAuthor(@PathVariable int authorID) {
            try {
                authorsService.deleteAuthor(authorID);
                return new ResponseEntity<>("Author deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error occurred while deleting author", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
                System.out.println("Validation error in field '" + fieldName + "': " + errorMessage);
            });
            return errors;
      }
        }
