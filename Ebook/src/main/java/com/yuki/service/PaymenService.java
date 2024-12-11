package com.yuki.service;

import com.yuki.dto.PaymentMethodDTO;
import com.yuki.entity.PaymentMethod;
import com.yuki.repositoty.PaymentMethodDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymenService {
    @Autowired
    private PaymentMethodDAO paymentMethodDAO;

    public PaymentMethod createPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethodName(paymentMethodDTO.getMethodName());
        PaymentMethod savedPaymentMethod = paymentMethodDAO.save(paymentMethod);
        return savedPaymentMethod;
    }

    public PaymentMethod updatePaymentMethod(int ID, PaymentMethodDTO paymentMethodDTO) {
        Optional<PaymentMethod> paymentMethod = paymentMethodDAO.findById(ID);
        if (paymentMethod.isPresent()) {
            PaymentMethod savedPaymentMethod = paymentMethod.get();
            savedPaymentMethod.setMethodName(paymentMethodDTO.getMethodName());
            return savedPaymentMethod;
        }
        return null;
    }
}
