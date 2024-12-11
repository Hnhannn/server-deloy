package com.yuki.Rest.Controller;

import com.yuki.dto.AddressDTO;
import com.yuki.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yuki.entity.Address;
import com.yuki.repositoty.AddressDAO;

import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class AddressRestController {
    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private AddressService addressService;

    // API get All dữ liệu Address
    @GetMapping("/address")
    public List<Address> address() {
        return addressDAO.findAll();
    }

    @GetMapping("/address/{id}")
    public Address address(@PathVariable int id) {
        Optional<Address> address = addressDAO.findById(id);
        return address.orElse(null);
    }

    // API Create Address
    @PostMapping("/address")
    public ResponseEntity<?> addressCreate(@Valid @RequestBody AddressDTO address) {
        try {
            // Kiểm tra nếu userId không hợp lệ (người dùng không tồn tại)
            if (address.getUserId() == 0) {
                return new ResponseEntity<>("User ID không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            // Tạo địa chỉ mới thông qua service
            Address createdAddress = addressService.createAddress(address);

            return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Đã xảy ra lỗi khi tạo địa chỉ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // API Update Address
    @PutMapping("/address/{addressID}")
    public ResponseEntity<?> updateAddress(@PathVariable int addressID, @Valid @RequestBody AddressDTO address) {
        try {
            Address updatedAddress = addressService.updateAddress(addressID, address);
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // API Delete Address
    @DeleteMapping("/address/{addressID}")
    public void deleteAddress(@PathVariable int addressID) {
        addressDAO.deleteById(addressID);
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

    // API get Addresses by UserId
    @GetMapping("/address/user/{userId}")
    public ResponseEntity<List<Address>> getAddressesByUserId(@PathVariable int userId) {
        try {
            List<Address> addresses = addressService.getAddressesByUserId(userId);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/address/{userId}/{addressID}")
    public ResponseEntity<?> updateAddressByUserId(@PathVariable int userId, @PathVariable int addressID, @Valid @RequestBody AddressDTO address) {
        try {
            address.setUserId(userId); // Đảm bảo userId được thiết lập trong AddressDTO
            Address updatedAddress = addressService.updateAddress(addressID, address);
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}