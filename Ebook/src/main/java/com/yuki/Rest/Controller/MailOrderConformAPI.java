package com.yuki.Rest.Controller;

import com.yuki.entity.Order;
import com.yuki.entity.OrderDetail;
import com.yuki.repositoty.OrderDAO;
import com.yuki.repositoty.OrderDetailDAO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/mailorder")
public class MailOrderConformAPI {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OrderDAO orderDetailDAO;

    @PostMapping("/SendConformOrder")
    public void sendConformEmail(@RequestBody EmailRequest emailRequest) {
        Optional<Order> optionalOrder = orderDetailDAO.findById(emailRequest.getOrderId());
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Chuyển đổi Order sang OrderDTO
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setUserName(order.getUser().getFullName());
            orderDTO.setOrderDate(order.getOrderDate().toString());
            orderDTO.setOrderStatus(order.getOrderStatus());
            orderDTO.setTotalAmount(order.getTotalAmount().doubleValue());

            // Chuyển đổi danh sách OrderDetail sang OrderDetailDTO
            List<OrderDetailDTO> orderDetailDTOs = order.getOrderDetails().stream()
                    .map(orderDetail -> {
                        OrderDetailDTO detailDTO = new OrderDetailDTO();
                        detailDTO.setBookTitle(orderDetail.getBook().getTitle());
                        detailDTO.setBookImage(orderDetail.getBook().getBookImage());
                        detailDTO.setQuantity(orderDetail.getQuantity());
                        detailDTO.setPrice(orderDetail.getPrice());
                        return detailDTO;
                    })
                    .collect(Collectors.toList());

            orderDTO.setOrderDetails(orderDetailDTOs);

            // In thông tin để kiểm tra
            logger.info("Order DTO: " + orderDTO);

            // Truyền DTO vào email
            sendEmail(orderDTO, emailRequest);
        } else {
            logger.warn("Order not found for ID: " + emailRequest.getOrderId());
        }
    }


    // Send email with OrderDTO and EmailRequest
    private void sendEmail(OrderDTO orderDTO, EmailRequest emailRequest) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(emailRequest.getTo());
            helper.setSubject("Cảm ơn bạn đã đặt hàng tại YUKI");
            helper.setText(buildEmailContent(orderDTO, emailRequest), true); // true to send HTML content
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
        }
    }


    private String buildEmailContent(OrderDTO orderDTO, EmailRequest emailRequest) {
        String body = """
                    <html>
                    <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Thông Tin Đơn Hàng</title>
                      <style>
                        body {
                          font-family: Arial, sans-serif;
                          margin: 0;
                          padding: 0;
                          background-color: #f9f9f9;
                        }
                        .order-container {
                          max-width: 800px;
                          margin: 2rem auto;
                          padding: 1.5rem;
                          background: #ffffff;
                          border-radius: 8px;
                          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                          position: relative;
                        }
                
                        h1 {
                          text-align: center;
                          color: #333;
                        }
                
                        .company-logo {
                          position: absolute;
                          top: 20px;
                          left: 20px;
                          width: 40px;
                        }
                
                        .company-logo img {
                          width: 60px;
                        }
                
                        .order-details {
                          margin-top: 1rem;
                        }
                
                        .address,
                        .products,
                        .total {
                          margin-bottom: 1.5rem;
                        }
                
                        h2 {
                          font-size: 1.25rem;
                          margin-bottom: 0.5rem;
                          color: #555;
                        }
                
                        p {
                          margin: 0.25rem 0;
                          color: #666;
                        }
                
                        table {
                          width: 100%;
                          border-collapse: collapse;
                          margin-top: 0.5rem;
                        }
                
                        table th,
                        table td {
                          padding: 0.75rem;
                          text-align: left;
                          border: 1px solid #ddd;
                        }
                
                        table th {
                          background-color: #f4f4f4;
                          color: #333;
                        }
                
                        table img {
                          width: 50px;
                          height: auto;
                          border-radius: 4px;
                        }
                
                        .total {
                                       display: grid;
                                       grid-template-columns: auto auto;
                
                                   }
                
                                   .total h2 {
                                       margin: 0; /* Xóa khoảng cách thừa */
                                       font-size: 1.25rem; /* Kích thước chữ */
                                       color: #333;
                                   }
                
                                   .total p {
                                       margin: 0; /* Xóa khoảng cách thừa */
                                       font-size: 1.5rem; /* Kích thước chữ lớn hơn */
                                       color: #e63946; /* Màu chữ đỏ nổi bật */
                                       font-weight: bold;
                                   }
                
                        .company-info {
                          margin-top: 2rem;
                          font-size: 0.875rem;
                          text-align: center;
                          color: #777;
                        }
                
                        .company-info p {
                          margin: 0.25rem 0;
                        }
                      </style>
                    </head>
                    <body>
                      <div class="order-container">
                        <div class="company-logo">
                          <img src="https://firebasestorage.googleapis.com/v0/b/ebookpage-836d8.appspot.com/o/file%2Fa8524710-ba81-4f45-a83d-64d6b167dbcd?alt=media&token=665afc1b-00f8-4dad-ad08-c9c8d5d15937" alt="Logo công ty" />
                        </div>
                
                        <h1>Thông Tin Đơn Hàng</h1>
                
                        <div class="order-details">
                          <div class="address">
                            <h2>Địa chỉ giao hàng</h2>
                            <p><strong>Tên:</strong> FULL_NAME</p>
                            <p><strong>Địa chỉ:</strong> ADDRESS</p>
                            <p><strong>Số điện thoại:</strong> PHONE_NUMBER</p>
                          </div>
                
                          <div class="products">
                            <h2>Sản phẩm</h2>
                            <table>
                              <thead>
                                <tr>
                                  <th>Hình ảnh</th>
                                  <th>Sản phẩm</th>
                                  <th>Số lượng</th>
                                  <th>Đơn giá</th>
                                  <th>Tổng</th>
                                </tr>
                              </thead>
                               <tbody>
                                                                      PRODUCTS_PLACEHOLDER
                                                                  </tbody>
                            </table>
                          </div>
                
                          <div class="total">
                            <h2>Tổng tiền</h2>
                            <p><strong>TOTAL</strong></p>
                          </div>
                        </div>
                
                        <div class="company-info">
                                                <p>© 2024 YuKi - Đam Mê Đọc Sách, Kết Nối Tri Thức!</p>
                                                <p>Địa chỉ: Tầng 5,Toà nhà FPT Polytechnic, đường số 22, phường Thường Thạnh, quận Cái Răng, TP Cần Thơ.</p>
                                                <p>Số điện thoại: 0374003493</p>
                                                <p>Email: support@yuki.com</p>
                                              </div>
                      </div>
                    </body>
                    </html>
                """;

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")); // Tiếng Việt, Việt Nam
        body = body.replace("ADDRESS", Objects.requireNonNullElse(emailRequest.getAddress(), "N/A"));
        body = body.replace("FULL_NAME", Objects.requireNonNullElse(emailRequest.getFullName(), "N/A"));
        body = body.replace("PHONE_NUMBER", Objects.requireNonNullElse(emailRequest.getPhoneNumber(), "N/A"));
        // Xây dựng nội dung danh sách sản phẩm
        StringBuilder productsHtml = new StringBuilder();
        for (OrderDetailDTO detail : orderDTO.getOrderDetails()) {
            double total = detail.getQuantity() * detail.getPrice();
            productsHtml.append("<tr>")
                    .append("<td><img src=\"").append(detail.getBookImage()).append("\" alt=\"").append(detail.getBookTitle()).append("\"></td>")
                    .append("<td>").append(detail.getBookTitle()).append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(currencyFormat.format(detail.getPrice())).append("</td>")
                    .append("<td>").append(currencyFormat.format(total)).append("</td>")
                    .append("</tr>");
        }

        // Thay thế chỗ chứa danh sách sản phẩm
        body = body.replace("PRODUCTS_PLACEHOLDER", productsHtml.toString());
        body = body.replace("TOTAL", currencyFormat.format((double) orderDTO.getTotalAmount()));
        return body;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OrderDetailDTO {
        private String bookTitle;
        private String bookImage;
        private int quantity;
        private double price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OrderDTO {
        private String userName;
        private String orderDate;
        private String orderStatus;
        private double totalAmount;
        private List<OrderDetailDTO> orderDetails;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailRequest {
        private String to;
        private String fullName;
        private String address;
        private String phoneNumber;
        private Integer orderId;
    }
}
