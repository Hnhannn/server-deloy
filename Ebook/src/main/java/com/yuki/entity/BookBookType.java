package com.yuki.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "BookBookTypes") // Đã sửa tên bảng
public class BookBookType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookBookTypeID")
    private int bookBookTypeID;

    @Column(name = "AccessType")
    private String accessType = "Miễn phí";

    @ManyToOne
    @JoinColumn(name = "BookID")
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "BookTypeID")
    @JsonManagedReference
    private BookType bookType;


    public BookBookType() {
        // Default constructor
    }

    public BookBookType(Book book, BookType bookType) {
        this.book = book;
        this.bookType = bookType;
    }

    public int getBookTypeID() {
        return bookType.getBookTypeID();
    }
}