package com.yuki.Rest.Controller;

import com.yuki.dto.ReviewsDTO;
import com.yuki.entity.Review;
import com.yuki.repositoty.ReviewDAO;
import com.yuki.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewDAO reviewDAO;

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return reviewDAO.findAll();
    }

    @GetMapping("/reviews/{bookID}")
    public List<Review> getReviewsByBookID(@PathVariable int bookID) {
        return reviewDAO.findByBook_BookID(bookID);
    }


    @PostMapping("/reviews")
    public ResponseEntity<Review> createReview(@RequestBody ReviewsDTO reviewDTO) {
        Review review = reviewService.createReview(reviewDTO);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable int reviewId, @RequestBody ReviewsDTO reviewDTO) {
        Review updatedReview = reviewService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }
}