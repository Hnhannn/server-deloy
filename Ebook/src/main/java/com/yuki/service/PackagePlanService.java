package com.yuki.service;

import com.yuki.dto.PackagePlansDTO;
import com.yuki.entity.PackagePlan;
import com.yuki.repositoty.PackagePlanDAO;
import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PackagePlanService {

    @Autowired
    private PackagePlanDAO packagePlanDAO;

    // Kiểm tra tên gói cước đã tồn tại hay chưa
    private boolean isPackageNameExists(String planName) {
        return packagePlanDAO.existsByPlanName(planName);
    }

    // Phương thức xử lý giá tiền (Định dạng lại giá tiền)
    private void formatPrice(PackagePlansDTO packagePlan) {
        if (packagePlan.getPrice() != null && packagePlan.getPrice() < 1000) {
            // Nếu giá nhập vào nhỏ hơn 1000, nhân với 1000 để chuyển đổi thành giá tiền Việt Nam
            packagePlan.setPrice(packagePlan.getPrice() * 1000);
        }
    }

    public PackagePlan create(PackagePlansDTO packagePlan) {
        // Kiểm tra nếu tên gói cước đã tồn tại
        if (isPackageNameExists(packagePlan.getPlanName())) {
            throw new IllegalArgumentException("Tên gói cước đã tồn tại, vui lòng chọn tên khác.");
        }

        // Xử lý giá tiền trước khi lưu
        formatPrice(packagePlan);

        // Nếu tên gói cước không trùng, tiến hành tạo gói cước mới
        PackagePlan plan = new PackagePlan();
        plan.setPlanName(packagePlan.getPlanName());
        plan.setPrice(packagePlan.getPrice());
        plan.setDuration(packagePlan.getDuration());
        plan.setStatus(true);
        return packagePlanDAO.save(plan);
    }

    public PackagePlan updategePlan(int ID, PackagePlansDTO packagePlan) {
        Optional<PackagePlan> planOptional = packagePlanDAO.findById(ID);
        if (planOptional.isPresent()) {
            formatPrice(packagePlan);

            PackagePlan planToUpdate = planOptional.get();
            planToUpdate.setPlanName(packagePlan.getPlanName());
            planToUpdate.setPrice(packagePlan.getPrice());
            planToUpdate.setDuration(packagePlan.getDuration());
            planToUpdate.setStatus(true);
            return packagePlanDAO.save(planToUpdate);
        }
        return null;
    }

    public void softDeletePackagePlan(int planID) {
        Optional<PackagePlan> planOptional = packagePlanDAO.findById(planID);
        if (planOptional.isPresent()) {
            PackagePlan planToDelete = planOptional.get();
            planToDelete.setStatus(false);
            packagePlanDAO.save(planToDelete);
        } else {
            throw new IllegalArgumentException("Gói cước không tồn tại.");
        }
    }
}