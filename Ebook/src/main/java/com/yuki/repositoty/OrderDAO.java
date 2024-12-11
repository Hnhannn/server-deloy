package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    List<Order> findByUser_UserID(Integer userId);
//    @Query("SELECT SUM(s.quantity) FROM Order s")
//    Long getTotalBooksSold();
}