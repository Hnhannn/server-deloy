package com.yuki.Rest.Controller;

import com.yuki.dto.OrdersDTO;
import com.yuki.entity.Order;
import com.yuki.entity.OrderDetail;
import com.yuki.service.OrderDetailService;
import com.yuki.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    // API get All dữ liệu Order
    @GetMapping("/order")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // API Create Order
    @PostMapping("/order")
    public ResponseEntity<Order> addOrder(@RequestBody OrdersDTO ordersDTO) {
        try {
            Order order = orderService.createOrder(ordersDTO);
            return new ResponseEntity<>(order, HttpStatus.CREATED); // Trả về đơn hàng mới với mã trạng thái 201
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Trả về lỗi 500 nếu có vấn đề xảy ra
        }
    }

    // API Update Order
    @PutMapping("/order/{orderID}")
    public ResponseEntity<Order> updateOrder(@PathVariable int orderID, @RequestBody OrdersDTO ordersDTO) {
        try {
            Order updatedOrder = orderService.updateOrder(orderID, ordersDTO);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK); // Trả về đơn hàng đã cập nhật với mã trạng thái 200
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Trả về lỗi 500 nếu có vấn đề xảy ra
        }
    }

    // API Delete Order
    @DeleteMapping("/order/{orderID}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int orderID) {
        try {
            orderService.deleteOrder(orderID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Trả về mã trạng thái 204 khi xóa thành công
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Trả về lỗi 500 nếu có vấn đề xảy ra
        }
    }

    // API get Orders by UserId
    @GetMapping("/order/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Integer userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK); // Trả về danh sách đơn hàng với mã trạng thái 200
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Trả về lỗi 500 nếu có vấn đề xảy ra
        }
    }

    @PutMapping("/order/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable int orderId, @RequestBody String status) {
        try {
            orderService.updateOrderStatus(orderId, status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API để lấy Orders và OrderDetails theo UserId
    @GetMapping("/order/user/{userId}/details")
    public ResponseEntity<List<Order>> getOrdersWithDetailsByUserId(@PathVariable Integer userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            for (Order order : orders) {
                List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getOrderID());
                order.setOrderDetails(orderDetails);
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // In OrderRestController
    @GetMapping("/order/{orderId}/details")
    public ResponseEntity<Order> getOrderWithDetailsByOrderId(@PathVariable Integer orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order != null) {
                List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
                order.setOrderDetails(orderDetails);
                return new ResponseEntity<>(order, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}