package com.yuki.service;

import com.yuki.dto.CartDetailDTO;
import com.yuki.entity.Book;
import com.yuki.entity.CartDetail;
import com.yuki.entity.User;
import com.yuki.repositoty.BookDAO;
import com.yuki.repositoty.CartDetailsDAO;
import com.yuki.repositoty.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartDetailService {

    @Autowired
    private CartDetailsDAO cartDetailsDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BookDAO bookDAO;

    public CartDetail createCartDetail(CartDetailDTO cartDetailDTO) {
        User user = userDAO.findById(cartDetailDTO.getUserID()).orElse(null);
        Book book = bookDAO.findById(cartDetailDTO.getBookID()).orElse(null);

        if (user == null || book == null) {
            throw new IllegalArgumentException("User or Book not found");
        }

        CartDetail existingCartDetail = cartDetailsDAO.findByUserAndBook(user, book);
        if (existingCartDetail != null) {
            existingCartDetail.setQuantity(existingCartDetail.getQuantity() + cartDetailDTO.getQuantity());
            return cartDetailsDAO.save(existingCartDetail);
        } else {
            CartDetail cartDetail = new CartDetail();
            cartDetail.setQuantity(cartDetailDTO.getQuantity());
            cartDetail.setUser(user);
            cartDetail.setBook(book);
            return cartDetailsDAO.save(cartDetail);
        }
    }

    public void deleteAllCartDetailsByUserId(int userId) {
        User user = userDAO.findById(userId).orElse(null);
        if (user != null) {
            List<CartDetail> cartDetails = cartDetailsDAO.findByUser_UserID(userId);
            cartDetailsDAO.deleteAll(cartDetails);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public List<CartDetail> getAllCartDetails() {
        return cartDetailsDAO.findAll();
    }

    public CartDetail getCartDetailById(int id) {
        return cartDetailsDAO.findById(id).orElse(null);
    }

    public CartDetail updateCartDetail(int id, CartDetailDTO cartDetailDTO) {
        CartDetail cartDetail = cartDetailsDAO.findById(id).orElse(null);
        if (cartDetail != null) {
            User user = userDAO.findById(cartDetailDTO.getUserID()).orElse(null);
            Book book = bookDAO.findById(cartDetailDTO.getBookID()).orElse(null);

            if (user == null || book == null) {
                throw new IllegalArgumentException("User or Book not found");
            }

            cartDetail.setQuantity(cartDetailDTO.getQuantity());
            cartDetail.setUser(user);
            cartDetail.setBook(book);
            return cartDetailsDAO.save(cartDetail);
        }
        return null;
    }

    public void deleteCartDetail(int id) {
        cartDetailsDAO.deleteById(id);
    }
}