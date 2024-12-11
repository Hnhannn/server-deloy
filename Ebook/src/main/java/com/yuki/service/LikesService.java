package com.yuki.service;

import com.yuki.dto.CommentsDTO;
import com.yuki.entity.Book;
import com.yuki.entity.Likes;
import com.yuki.entity.User;
import com.yuki.repositoty.BookDAO;
import com.yuki.repositoty.LikesDAO;
import com.yuki.repositoty.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikesService {
    @Autowired
    private LikesDAO likesDAO;
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private UserDAO userDAO;

    public Likes createComment(CommentsDTO commentsDTO) {
        if (commentsDTO == null || commentsDTO.getBookId() == 0 || commentsDTO.getUserId() == 0) {
            throw new IllegalArgumentException("Invalid comment data provided.");
        }

        // Lấy book từ database, nếu không có thì ném lỗi
        Book book = bookDAO.findById(commentsDTO.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + commentsDTO.getBookId()));

        // Lấy user từ database, nếu không có thì ném lỗi
        User user = userDAO.findById(commentsDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + commentsDTO.getUserId()));

        // Tạo comment và gán giá trị
        Likes comment = new Likes();
        comment.setLikeTime(LocalDateTime.now());
        comment.setBook(book);
        comment.setUser(user);
       return likesDAO.save(comment);
    }


    public void deleteComment(int commentId) {
        likesDAO.deleteById(commentId);
    }

}
