package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.Author;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorDAO extends JpaRepository<Author, Integer> {
    Boolean existsByAuthorName(String authorname);
    List<Author> findByStatusTrue();
}