package com.yuki.repositoty;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {
    boolean existsByCategoryName(String categoryName);
    List<Category> findByStatusTrue();
}