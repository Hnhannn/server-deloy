package com.yuki.service;

import com.yuki.dto.OrderDetailsDTO;
import com.yuki.dto.OrdersDTO;
import com.yuki.entity.*;
import com.yuki.repositoty.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PaymentMethodDAO paymentMethodDAO;


    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public Order createOrder(OrdersDTO orderDTO) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setTotalAmount(BigDecimal.valueOf(orderDTO.getTotalAmount()));
        User user = userDAO.findById(orderDTO.getUserId()).orElse(null);
        PaymentMethod paymentMethod = paymentMethodDAO.findById(orderDTO.getPaymentMethodId()).orElse(null);
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        return orderDAO.save(order);
    }

    public Order updateOrder(int orderID, OrdersDTO orderDTO) {
        Order existingOrder = orderDAO.findById(orderID).orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setOrderStatus(orderDTO.getOrderStatus());
        existingOrder.setTotalAmount(BigDecimal.valueOf(orderDTO.getTotalAmount()));
        User user = userDAO.findById(orderDTO.getUserId()).orElse(null);
        PaymentMethod paymentMethod = paymentMethodDAO.findById(orderDTO.getPaymentMethodId()).orElse(null);

        existingOrder.setUser(user);
        existingOrder.setPaymentMethod(paymentMethod);

        return orderDAO.save(existingOrder);
    }

    public void deleteOrder(int orderID) {
        orderDAO.deleteById(orderID);
    }

    public List<Order> getOrdersByUserId(Integer userId) {
        return orderDAO.findByUser_UserID(userId);
    }

    public void updateOrderStatus(int orderId, String status) {
        Optional<Order> order = orderDAO.findById(orderId);
        if (order.isPresent()) {
            Order existingOrder = order.get();
            existingOrder.setOrderStatus(status);
            orderDAO.save(existingOrder);
        }
    }

    public Order getOrderById(Integer orderId) {
        return orderDAO.findById(orderId).orElse(null);
    }
}