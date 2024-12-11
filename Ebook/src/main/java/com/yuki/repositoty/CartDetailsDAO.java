package com.yuki.repositoty;

import com.yuki.entity.Book;
import com.yuki.entity.CartDetail;
import com.yuki.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailsDAO extends JpaRepository<CartDetail, Integer> {
    CartDetail findByUserAndBook(User user, Book book);
    List<CartDetail> findByUser_UserID(int userID);
}
