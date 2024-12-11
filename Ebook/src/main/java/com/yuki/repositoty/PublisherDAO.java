package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.Publisher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherDAO extends JpaRepository<Publisher, Integer> {
    boolean existsByPublisherName(String publisherName);
    List<Publisher> findByStatusTrue();
}