package com.yuki.service;

import java.time.LocalDateTime;

// import com.yuki.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuki.dto.ReviewsDTO;



import com.yuki.entity.Book;
import com.yuki.entity.Order;
import com.yuki.entity.ReadBook;
import com.yuki.entity.Review;

import com.yuki.entity.User;


import com.yuki.repositoty.BookDAO;
import com.yuki.repositoty.OrderDAO;
import com.yuki.repositoty.ReadBookDAO;
import com.yuki.repositoty.ReviewDAO;
import com.yuki.repositoty.UserDAO;
// import com.yuki.repositoty.OrderDAO;

@Service
public class ReviewService {
    @Autowired
    private ReviewDAO reviewDAO;
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ReadBookDAO readBookDAO;

    public Review createReview(ReviewsDTO reviewDTO) {
        if (reviewDTO == null || reviewDTO.getBookId() == 0 || reviewDTO.getUserId() == 0 || reviewDTO.getRating() == 0
                || reviewDTO.getReviewContent() == null) {
            throw new IllegalArgumentException("Invalid review data provided.");
        }

        // Lấy book từ database, nếu không có thì ném lỗi
        Book book = bookDAO.findById(reviewDTO.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + reviewDTO.getBookId()));

        // Lấy user từ database, nếu không có thì ném lỗi
        User user = userDAO.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + reviewDTO.getUserId()));

        // Tạo review và gán giá trị
        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setReviewContent(reviewDTO.getReviewContent());
        review.setReviewDate(LocalDateTime.now());
        review.setBook(book);
        review.setUser(user);
        return reviewDAO.save(review);
    }

    public Review updateReview(int reviewId, ReviewsDTO reviewDTO) {
        if (reviewDTO == null || reviewDTO.getBookId() == 0 || reviewDTO.getUserId() == 0 || reviewDTO.getRating() == 0
                || reviewDTO.getReviewContent() == null) {
            throw new IllegalArgumentException("Invalid review data provided.");
        }

        // Find existing review
        Review review = reviewDAO.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        // Update review fields
        review.setRating(reviewDTO.getRating());
        review.setReviewContent(reviewDTO.getReviewContent());
        review.setReviewDate(LocalDateTime.now());

        // Update book if necessary
        if (review.getBook().getBookID() != reviewDTO.getBookId()) {
            Book book = bookDAO.findById(reviewDTO.getBookId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Book not found with ID: " + reviewDTO.getBookId()));
            review.setBook(book);
        }

        // Update user if necessary
        if (review.getUser().getUserID() != reviewDTO.getUserId()) {
            User user = userDAO.findById(reviewDTO.getUserId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("User not found with ID: " + reviewDTO.getUserId()));
            review.setUser(user);
        }

        return reviewDAO.save(review);
    }
}
