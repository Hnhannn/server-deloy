package com.yuki.Rest.Controller;

import com.yuki.dto.PackagePlansDTO;
import com.yuki.service.PackagePlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.yuki.entity.PackagePlan;
import com.yuki.repositoty.PackagePlanDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class PackagePlanRestController {

    @Autowired
    private PackagePlanDAO packagePlanDAO;

    @Autowired
    private PackagePlanService packagePlanService;

    // API get All dữ liệu PackagePlan
    @GetMapping("/packagePlan")
    public List<PackagePlan> packagePlan() {
        return packagePlanDAO.findByStatus(true);
    }

    @GetMapping("/packagePlan/{id}")
    public PackagePlan packagePlan(@PathVariable int id) {
        Optional<PackagePlan> packagePlan = packagePlanDAO.findById(id);
        return packagePlan.orElse(null);  // Return null if not found
    }

    // API Create PackagePlan
    @PostMapping("/packagePlan")
    public ResponseEntity<?> packagePlanCreate(@Valid @RequestBody PackagePlansDTO packagePlan) {
        try {
            PackagePlan createdPlan = packagePlanService.create(packagePlan);
            return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi tên gói cước bị trùng
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            return new ResponseEntity<>("Có lỗi xảy ra khi thêm gói cước.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API Update PackagePlan
    @PutMapping("/packagePlan/{planID}")
    public ResponseEntity<?> updatePackagePlan(@PathVariable int planID, @Valid @RequestBody PackagePlansDTO packagePlan) {
        try {
            PackagePlan updatedPlan = packagePlanService.updategePlan(planID, packagePlan);
            return new ResponseEntity<>(updatedPlan, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi tên gói cước bị trùng khi cập nhật
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật gói cước.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API Delete PackagePlan
    @DeleteMapping("/packagePlan/{planID}")
    public ResponseEntity<?> deletePackagePlan(@PathVariable int planID) {
        try {
            packagePlanService.softDeletePackagePlan(planID);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi xóa gói cước.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    // Handle IllegalArgumentException (Trùng tên gói)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}