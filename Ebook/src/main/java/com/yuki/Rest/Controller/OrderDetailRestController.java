package com.yuki.Rest.Controller;


import com.yuki.dto.OrderDetailsDTO;
import com.yuki.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuki.entity.OrderDetail;
import com.yuki.repositoty.OrderDetailDAO;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class OrderDetailRestController {
    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Autowired
    private OrderDetailService orderService;

    //API get All dữ liệu OrderDetail
    @GetMapping("/orderDetail")
    public List<OrderDetail> orderDetail(){
        return orderDetailDAO.findAll();
    }

    // API Create OrderDetail
    @PostMapping("/orderDetail")
    public List<OrderDetail> orderDetailCreate(@RequestBody List<OrderDetailsDTO> orderDetails) {
        return orderDetails.stream()
                .map(orderService::createOrderDetail)
                .collect(Collectors.toList());
    }

    @PostMapping("/orderDetailOne")
    public OrderDetail orderDetailCreateOne(@RequestBody OrderDetailsDTO orderDetails) {
        return orderService.createOrderDetail(orderDetails);
    }

    //API Update
    @PutMapping("/orderDetail/{orderDetailID}")
    public OrderDetail updateOrderDetail(@PathVariable int orderDetailID, @RequestBody OrderDetail orderDetail) {
        orderDetail.setOrderDetailID(orderDetailID);
        return orderDetailDAO.save(orderDetail);
    }

    @GetMapping("/orderDetail/{bookID}/{userID}")
    public List<OrderDetail> getOrderDetailsByBookIDAndUserID(@PathVariable int bookID, @PathVariable int userID) {
        return orderDetailDAO.findByBook_BookIDAndOrder_User_UserID(bookID, userID);
    }

    //API Delete
    @DeleteMapping("/orderDetail/{orderDetailID}")
    public void deleteOrderDetail(@PathVariable int orderDetailID) {
        orderDetailDAO.deleteById(orderDetailID);
    }
}