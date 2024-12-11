package com.yuki.repositoty;

import com.yuki.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LikesDAO extends JpaRepository<Likes, Integer> {
    Likes findByBook_BookIDAndUser_UserID(Integer bookID, Integer userID);
    List<Likes> findByUser_UserID(Integer userID);
}