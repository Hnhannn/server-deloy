package com.yuki.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "BookTypes")
public class BookType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookTypeID")
    private int bookTypeID;

    @Column(name = "TypeName")
    private String typeName;

    @OneToMany(mappedBy = "bookType")
    @JsonBackReference
    private List<BookBookType> bookBookTypes;
}