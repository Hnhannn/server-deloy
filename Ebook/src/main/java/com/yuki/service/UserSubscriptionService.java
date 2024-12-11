package com.yuki.service;

// import com.yuki.dto.UserDto;
import com.yuki.dto.UserSubscriptionsDTO;
import com.yuki.entity.PackagePlan;
import com.yuki.entity.User;
import com.yuki.entity.UserSubscription;
import com.yuki.repositoty.PackagePlanDAO;
import com.yuki.repositoty.UserDAO;
import com.yuki.repositoty.UserSubscriptionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
// import java.util.Optional;

@Service
public class UserSubscriptionService {
    @Autowired
    private UserSubscriptionDAO userSubscriptionDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PackagePlanDAO packagePlanDAO;



    public UserSubscription createSubscription(UserSubscriptionsDTO subscriptionDTO) {
        User user = userDAO.findById(subscriptionDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        PackagePlan packagePlan = packagePlanDAO.findById(subscriptionDTO.getPlanId()).orElseThrow(() -> new RuntimeException("Package plan not found"));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate;

        List<UserSubscription> userSubscriptions = user.getuserSubscription();
        UserSubscription activeSubscription = userSubscriptions.stream()
                .filter(sub -> "Hoạt động".equals(sub.getStatus()))
                .findFirst()
                .orElse(null);
        if (activeSubscription != null) {
            startDate = activeSubscription.getEndDate().isAfter(LocalDateTime.now())
                    ? activeSubscription.getEndDate()
                    : LocalDateTime.now();
        }

        if (packagePlan.getDuration() <= 30) {
            endDate = startDate.plusDays(packagePlan.getDuration());
        } else if (packagePlan.getDuration() < 90) {
            endDate = startDate.plusWeeks(packagePlan.getDuration() / 7);
        } else {
            endDate = startDate.plusMonths(packagePlan.getDuration() / 30);
        }

        UserSubscription subscription = activeSubscription;
        if (subscription == null || !"Hoạt động".equals(subscription.getStatus())) {
            // Tạo đăng ký mới nếu chưa có đăng ký hoặc đăng ký cũ không hoạt động
            subscription = new UserSubscription();
            subscription.setUser(user);
            subscription.setPackagePlan(packagePlan);
            subscription.setStartDate(LocalDateTime.now());
            subscription.setSubscriptionDate(LocalDateTime.now());
        }

        // Cập nhật ngày kết thúc và trạng thái
        subscription.setEndDate(endDate);
        subscription.setStatus("Hoạt động");

        UserSubscription savedSubscription = userSubscriptionDAO.save(subscription);
        return savedSubscription;
    }


    public UserSubscription updateSubscription(int id, UserSubscriptionsDTO subscriptionDTO) {
        return userSubscriptionDAO.findById(id).map(subscription -> {
            User user = userDAO.findById(subscriptionDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            PackagePlan packagePlan = packagePlanDAO.findById(subscriptionDTO.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Package plan not found"));

            subscription.setUser(user);
            subscription.setPackagePlan(packagePlan);
            if (subscription.getEndDate().isBefore(LocalDateTime.now())) {
                subscription.setStatus("Hết Hạn");
            } else {
                subscription.setStatus("Hoạt động");
            }
            return userSubscriptionDAO.save(subscription);
        }).orElse(null);
    }

    public List<UserSubscription> getSubscriptionsByUserId(Integer userId) {
        return userSubscriptionDAO.findByUser_UserID(userId);
    }

    public Long getTotalUserSubscriptionID() {
        return userSubscriptionDAO.getTotalUserSubscriptionID();
    }
}
