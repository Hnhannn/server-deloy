package com.yuki.Rest.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.yuki.dto.PublishersDTO;
import com.yuki.service.PublishersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.yuki.entity.Publisher;
import com.yuki.repositoty.PublisherDAO;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class PublisherRestController {
    @Autowired
    private PublisherDAO publisherDAO;

    @Autowired
    private PublishersService publishersService;

    // API get All dữ liệu Publishers
    @GetMapping("/publisher")
    public List<Publisher> getAllActivePublishers() {
        return publisherDAO.findByStatusTrue();
    }

    // API get All dữ liệu Publishers
    @GetMapping("/publisher/{id}")
    public Publisher publisherById(@PathVariable int id) {
        Optional<Publisher> publisher = publisherDAO.findById(id);
        if (publisher.isPresent()) {
            return publisher.get();
        }
        return null;
    }

    // API Create Publishers
    @PostMapping("/publisher")
    public ResponseEntity<?> publisherCreate(@Valid @RequestBody PublishersDTO publisher) {
        // Kiểm tra nếu name của publisher đã tồn tại
        if (Boolean.TRUE.equals(publisherDAO.existsByPublisherName(publisher.getPublisherName()))) {
            return new ResponseEntity<>("Tên người dùng đã được sử dụng cho " + publisher.getPublisherName(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Publisher createdPub = publishersService.createPublisher(publisher);
            return new ResponseEntity<>(createdPub, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi thêm nhà xuất bản.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API Update
    @PutMapping("/publisher/{publisherID}")
    public ResponseEntity<?> updatePublisher(@PathVariable int publisherID, @Valid @RequestBody PublishersDTO publisher) {
        // Kiểm tra nếu name của publisher đã tồn tại
        if (Boolean.TRUE.equals(publisherDAO.existsByPublisherName(publisher.getPublisherName()))) {
            return new ResponseEntity<>("Tên nhà xuất bản đã được sử dụng cho " + publisher.getPublisherName(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Publisher updatedPub = publishersService.updatePublisher(publisherID, publisher);
            return new ResponseEntity<>(updatedPub, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật nhà xuất bản.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // API Delete
    @DeleteMapping("/publisher/{publisherID}")
    public ResponseEntity<?> deletePublisher(@PathVariable int publisherID) {
        try {
            publishersService.deletePublisher(publisherID);
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
