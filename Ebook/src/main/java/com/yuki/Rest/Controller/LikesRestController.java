package com.yuki.Rest.Controller;


import com.yuki.dto.CommentsDTO;
import com.yuki.entity.Likes;
import com.yuki.repositoty.LikesDAO;
import com.yuki.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class LikesRestController {
    @Autowired
    private LikesDAO likesDAO;
    @Autowired
    private LikesService commentService;

    //API get All dữ liệu Author
    @GetMapping("/like")
    public List<Likes> likes(){
        return likesDAO.findAll();
    }

    //API Create Publishers
    @PostMapping("/like")
    public Likes likeCreate(@RequestBody CommentsDTO comment) {
        return commentService.createComment(comment);
    }

    // API to get comments by bookID
    @GetMapping("/like/{bookID}/{userID}")
    public Likes getCommentsByBookID(@PathVariable int bookID,@PathVariable int userID) {
        return likesDAO.findByBook_BookIDAndUser_UserID(bookID, userID);
    }

    @GetMapping("/like/{userID}")
    public List<Likes> getCommentsByUserID(@PathVariable int userID) {
        return likesDAO.findByUser_UserID(userID);
    }

    //API Delete
    @DeleteMapping("/like/{commentID}")
    public void deleteComment(@PathVariable int commentID) {
        likesDAO.deleteById(commentID);
    }
}