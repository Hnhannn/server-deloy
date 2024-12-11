package com.yuki.Rest.Controller;

import com.yuki.dto.CartDetailDTO;
import com.yuki.entity.CartDetail;
import com.yuki.repositoty.CartDetailsDAO;
import com.yuki.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/cartdetails")
public class CartDetailRestController {

    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private CartDetailsDAO cartDetailsDAO;

    // Create CartDetail
    @PostMapping
    public ResponseEntity<CartDetail> createCartDetail(@RequestBody CartDetailDTO cartDetailDTO) {
        try {
            CartDetail cartDetail = cartDetailService.createCartDetail(cartDetailDTO);
            return new ResponseEntity<>(cartDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all CartDetails
    @GetMapping
    public ResponseEntity<List<CartDetail>> getAllCartDetails() {
        try {
            List<CartDetail> cartDetails = cartDetailService.getAllCartDetails();
            return new ResponseEntity<>(cartDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get CartDetail by ID
    @GetMapping("/{id}")
    public ResponseEntity<CartDetail> getCartDetailById(@PathVariable("id") int id) {
        try {
            CartDetail cartDetail = cartDetailService.getCartDetailById(id);
            return new ResponseEntity<>(cartDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get CartDetail by ID
    @GetMapping("/user/{id}")
    public ResponseEntity<List<CartDetail>> getCartDetailsByUserID(@PathVariable("id") int id) {
        try {
            List<CartDetail> cartDetails = cartDetailsDAO.findByUser_UserID(id);
            return new ResponseEntity<>(cartDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    // Update CartDetail
    @PutMapping("/{id}")
    public ResponseEntity<CartDetail> updateCartDetail(@PathVariable("id") int id, @RequestBody CartDetailDTO cartDetailDTO) {
        try {
            CartDetail updatedCartDetail = cartDetailService.updateCartDetail(id, cartDetailDTO);
            return new ResponseEntity<>(updatedCartDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("deleteCart/{userId}")
    public void deleteAllCartDetailsByUserId(@PathVariable int userId) {
        cartDetailService.deleteAllCartDetailsByUserId(userId);
    }
}