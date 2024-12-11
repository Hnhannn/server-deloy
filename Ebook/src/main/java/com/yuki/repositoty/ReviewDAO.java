package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.Review;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewDAO extends JpaRepository<Review, Integer> {
    List<Review> findByBook_BookID(int bookID);
}