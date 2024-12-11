package com.yuki.Rest.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.yuki.dto.CategoriesDTO;
import com.yuki.service.CategoriesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.yuki.entity.Category;
import com.yuki.repositoty.CategoryDAO;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class CategoriesRestController {
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private CategoriesService categoriesService;

    //API get All dữ liệu Category with status = true
    @GetMapping("/category")
    public List<Category> getAllActiveCategories() {
        return categoryDAO.findByStatusTrue();
    }

    // API get All dữ liệu Categories
    @GetMapping("/category/{id}")
    public Category categoryById(@PathVariable int id) {
        Optional<Category> category = categoryDAO.findById(id);
        if (category.isPresent()) {
            return category.get();
        }
        return null;
    }

    @PostMapping("/category")
    public ResponseEntity<?> categoryCreate(@Valid @RequestBody CategoriesDTO category) {
        if (Boolean.TRUE.equals(categoryDAO.existsByCategoryName(category.getCategoryName()))) {
            return new ResponseEntity<>("Tên thể loại đã được sử dụng cho " + category.getCategoryName(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Category createdPub = categoriesService.createCategory(category);
            return new ResponseEntity<>(createdPub, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi thêm thể loại.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API Update
    @PutMapping("/category/{categoryID}")
    public ResponseEntity<?> updateCategory(@PathVariable int categoryID, @RequestBody CategoriesDTO category) {
        if (Boolean.TRUE.equals(categoryDAO.existsByCategoryName(category.getCategoryName()))) {
            return new ResponseEntity<>("Tên thể loại đã được sử dụng cho " + category.getCategoryName(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Category updatedPub = categoriesService.updateCategory(categoryID, category);
            return new ResponseEntity<>(updatedPub, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật thể loại.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API Delete
    @DeleteMapping("/category/{categoryID}")
    public ResponseEntity<?> deleteCategory(@PathVariable int categoryID) {
        try {
            categoriesService.deleteCategory(categoryID);
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while deleting category", HttpStatus.INTERNAL_SERVER_ERROR);
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
