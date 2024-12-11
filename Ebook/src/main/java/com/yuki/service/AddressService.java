package com.yuki.service;

import com.yuki.dto.AddressDTO;
import com.yuki.entity.Address;
import com.yuki.entity.User;
import com.yuki.repositoty.AddressDAO;
import com.yuki.repositoty.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressDAO addressDAO;
    @Autowired
    private UserDAO userDAO;

    public Address createAddress(AddressDTO addressDTO) {
        validateAddress(addressDTO);

        Address address = new Address();
        User user = userDAO.findById(addressDTO.getUserId()).orElse(null);
        address.setUser(user);
        address.setAddressLine(addressDTO.getAddressLine());
        address.setWardCode(addressDTO.getSuburb());
        address.setDistrict(addressDTO.getDistrict());
        address.setCity(addressDTO.getCity());
        address.setPhoneNumber(addressDTO.getPhoneNumber());
        return addressDAO.save(address);
    }

    public Address updateAddress(int ID, AddressDTO addressDTO) {
        validateAddress(addressDTO);

        Optional<Address> address = addressDAO.findById(ID);
        if (address.isPresent()) {
            Address addressUP = address.get();
            User user = userDAO.findById(addressDTO.getUserId()).orElse(null);
            addressUP.setUser(user);
            addressUP.setAddressLine(addressDTO.getAddressLine());
            addressUP.setWardCode(addressDTO.getSuburb());
            addressUP.setDistrict(addressDTO.getDistrict());
            addressUP.setCity(addressDTO.getCity());
            addressUP.setPhoneNumber(addressDTO.getPhoneNumber());
            return addressDAO.save(addressUP);
        }
        return null;
    }

    private void validateAddress(AddressDTO addressDTO) {
        if (addressDTO.getPhoneNumber() == null || addressDTO.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được trống");
        }
        if (!addressDTO.getPhoneNumber().matches("^(0|\\+84)[3|5|7|8|9][0-9]{8}$")) {
            throw new IllegalArgumentException("Số điện thoại không đúng định dạng");
        }
        if (addressDTO.getCity() == 0) {
            throw new IllegalArgumentException("Chưa chọn tỉnh/thành phố");
        }
        if (addressDTO.getDistrict() == 0) {
            throw new IllegalArgumentException("Chưa chọn quận/huyện");
        }
        if (addressDTO.getSuburb() == 0) {
            throw new IllegalArgumentException("Chưa chọn phường/xã");
        }
        if (addressDTO.getAddressLine() == null || addressDTO.getAddressLine().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ cụ thể nhận hàng không được trống");
        }
        if (!addressDTO.getAddressLine().matches("^[a-zA-Z0-9À-ỹ\\s,.-/]+$")) {
            throw new IllegalArgumentException("Địa chỉ cụ thể nhận hàng chỉ có số, chữ và các ký tự hợp lệ như dấu - , dấu chấm, dấu phẩy, dấu /");
        }
    }

    public List<Address> getAddressesByUserId(int userId) {
        return addressDAO.findByUser_UserID(userId);
    }
}