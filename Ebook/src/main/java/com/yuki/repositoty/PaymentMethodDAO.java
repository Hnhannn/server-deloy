package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuki.entity.PaymentMethod;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodDAO extends JpaRepository<PaymentMethod, Integer> {

}