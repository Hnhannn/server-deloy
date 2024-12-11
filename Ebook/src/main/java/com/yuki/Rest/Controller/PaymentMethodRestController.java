package com.yuki.Rest.Controller;


import com.yuki.dto.PaymentMethodDTO;
import com.yuki.service.PaymenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuki.entity.PaymentMethod;
import com.yuki.repositoty.PaymentMethodDAO;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class PaymentMethodRestController {
    @Autowired
    private PaymentMethodDAO paymentMethodDAO;

    @Autowired
    private PaymenService paymenService;

    //API get All dữ liệu PaymentMethod
    @GetMapping("/paymentMethod")
    public List<PaymentMethod> PaymentMethod(){
        return paymentMethodDAO.findAll();
    }

    //API Create Publishers
    @PostMapping("/paymentMethod")
    public PaymentMethod paymentMethodCreate(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        return this.paymenService.createPaymentMethod(paymentMethodDTO);
    }

    //API Update
    @PutMapping("/paymentMethod/{paymentMethodID}")
    public PaymentMethod updatePaymentMethod(@PathVariable int paymentMethodID, @RequestBody PaymentMethodDTO paymentMethodDTO) {
        return paymenService.updatePaymentMethod(paymentMethodID, paymentMethodDTO);
    }

    //API Delete
    @DeleteMapping("/paymentMethod/{paymentMethodID}")
    public void deletePaymentMethod(@PathVariable int paymentMethodID) {
        paymentMethodDAO.deleteById(paymentMethodID);
    }
}