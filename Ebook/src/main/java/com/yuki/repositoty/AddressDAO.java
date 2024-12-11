package com.yuki.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuki.entity.Address;

import java.util.List;

@Repository
public interface AddressDAO extends JpaRepository<Address, Integer> {
    List<Address> findByUser_UserID(int userId);
}