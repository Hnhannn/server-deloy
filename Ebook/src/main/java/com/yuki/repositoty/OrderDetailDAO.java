 package com.yuki.repositoty;

 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

 import com.yuki.entity.OrderDetail;

 import java.util.List;

 @Repository
 public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer> {
  List<OrderDetail> findByBook_BookIDAndOrder_User_UserID(int bookID, int userID);

  List<OrderDetail> findByOrder_OrderID(int orderId);
 }