package com.yuki.Rest.Controller;


import com.yuki.entity.BookBookType;
import com.yuki.entity.BookType;
import com.yuki.repositoty.BookBookTypeDAO;
import com.yuki.repositoty.BookTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class BookTypeRestController {
    @Autowired
    private BookTypeDAO bookTypeDAO;

    @Autowired
    private BookBookTypeDAO bookBookTypeDAO;

    @GetMapping("/booktypes")
    public List<BookType> getBookTypes() {
        return bookTypeDAO.findAll();
    }

    @GetMapping("/bookBokTypes")
    public List<BookBookType> getBookBookTypes() {
        return bookBookTypeDAO.findByBook_BookIDAndBookType_BookTypeID(78, 2);
    }


}
