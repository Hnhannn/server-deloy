package com.yuki.service;

import com.yuki.dto.OrderDetailsDTO;
import com.yuki.entity.Book;
import com.yuki.entity.Order;
import com.yuki.entity.OrderDetail;
import com.yuki.repositoty.BookDAO;
import com.yuki.repositoty.OrderDAO;
import com.yuki.repositoty.OrderDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private BookDAO bookDAO;

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailDAO.findByOrder_OrderID(orderId);
    }

    public OrderDetail createOrderDetail(OrderDetailsDTO orderDetail) {
        OrderDetail newOrderDetail = new OrderDetail();
        newOrderDetail.setOrderDetailID(orderDetail.getOrderDetailId());
        Order order = orderDAO.findById(orderDetail.getOrderId()).orElse(null);
        newOrderDetail.setOrder(order);
        Book book = bookDAO.findById(orderDetail.getBookId()).orElse(null);
        newOrderDetail.setBook(book);
        newOrderDetail.setQuantity(orderDetail.getQuantity());
        newOrderDetail.setPrice(orderDetail.getPrice());
        return orderDetailDAO.save(newOrderDetail);
    }


}