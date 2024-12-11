package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.PackagePlan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackagePlanDAO extends JpaRepository<PackagePlan, Integer> {
    // Kiểm tra nếu gói cước đã tồn tại theo tên
    boolean existsByPlanName(String planName);

    List<PackagePlan> findByStatus(boolean status);
}