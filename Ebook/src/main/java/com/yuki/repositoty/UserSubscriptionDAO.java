package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.UserSubscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionDAO extends JpaRepository<UserSubscription, Integer> {
    List<UserSubscription> findByUser_UserID(Integer userId);
    @Query("SELECT SUM(u.userSubscriptionID) FROM UserSubscription u")
    Long getTotalUserSubscriptionID();
}