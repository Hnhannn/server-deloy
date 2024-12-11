package com.yuki.Rest.Controller;


import com.yuki.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin ("*")
@RequestMapping("/api/vnpay")
public class VNPayController {

    @Autowired
    private VNPayService vnpayService;

    @Autowired
    HttpServletRequest request;

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnpayService.createOrder(orderRequest.getTotal(), orderRequest.getOrderInfo(), baseUrl);
    }

    @GetMapping("/orderReturn")
    public int orderReturn(HttpServletRequest request) {
        return vnpayService.orderReturn(request);
    }

    @PostMapping("/pay/{ToTalValue}")
    public ResponseEntity<Map<String, String>> pay(@PathVariable("ToTalValue") float orderTotal) {
        int tong = (int) orderTotal;
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnpayService.createOrder(tong, "Thanh toán " + new Date(), baseUrl);
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", vnpayUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<String> handleVNPayReturn(@RequestParam Map<String, String> vnp_Params) {
        // Chuyển hướng về React với tất cả các tham số từ VNPay
        StringBuilder redirectUrl = new StringBuilder("http://localhost:9999/vnpay-payment?");

        vnp_Params.forEach((key, value) -> {
            redirectUrl.append(key).append("=").append(value).append("&");
        });

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl.toString())
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderRequest {
        private int total;
        private String orderInfo;
        private String urlReturn; // Add this field if needed
    }
}
