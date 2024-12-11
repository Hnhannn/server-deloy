package com.yuki.Rest.Controller;


import com.yuki.dto.UserSubscriptionsDTO;
import com.yuki.entity.UserSubscription;
import com.yuki.repositoty.UserSubscriptionDAO;
import com.yuki.service.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class UserSubscriptionRestController {
    @Autowired
    private UserSubscriptionDAO userSubscriptionDAO;
    @Autowired
    private UserSubscriptionService userSubscriptionService;

    // API get All dữ liệu Publishers
    @GetMapping("/userSubscription")
    public List<UserSubscription> userSubscription() {
        return userSubscriptionDAO.findAll();
    }

    // API lấy dữ liệu UserSubscription theo ID
    @GetMapping("/userSubscription/{id}")
    public ResponseEntity<UserSubscription> getUserSubscriptionById(@PathVariable int id) {
        Optional<UserSubscription> subscription = userSubscriptionDAO.findById(id);
        if (subscription.isPresent()) {
            return ResponseEntity.ok(subscription.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // API Create Publishers
    @PostMapping("/userSubscription")
    public UserSubscription publisherCreate(@RequestBody UserSubscriptionsDTO userSubscriptionsDTO) {
        return this.userSubscriptionService.createSubscription(userSubscriptionsDTO);
    }

    // API Update
    @PutMapping("/userSubscription/{userSubscriptionID}")
    public UserSubscription updateUserSubscription(@PathVariable int userSubscriptionID, @RequestBody UserSubscriptionsDTO userSubscription) {
        return userSubscriptionService.updateSubscription(userSubscriptionID, userSubscription);
    }

    // API Delete
    @DeleteMapping("/userSubscription/{userSubscriptionID}")
    public void deleteUserSubscription(@PathVariable int userSubscriptionID) {
        userSubscriptionDAO.deleteById(userSubscriptionID);
    }

    // API get UserSubscriptions by UserId
    @GetMapping("/userSubscription/user/{userId}")
    public ResponseEntity<List<UserSubscription>> getUserSubscriptionsByUserId(@PathVariable Integer userId) {
        try {
            List<UserSubscription> subscriptions = userSubscriptionService.getSubscriptionsByUserId(userId);
            return new ResponseEntity<>(subscriptions, HttpStatus.OK); // Trả về danh sách gói cước với mã trạng thái 200
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Trả về lỗi 500 nếu có vấn đề xảy ra
        }
    }
}
