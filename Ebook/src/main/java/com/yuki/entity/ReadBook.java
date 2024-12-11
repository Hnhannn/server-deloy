package com.yuki.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

// import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the ReadBooks database table.
 * 
 */
@Data @Getter @Setter
@Entity
@Table(name = "Readbooks")
@NamedQuery(name = "ReadBook.findAll", query = "SELECT r FROM ReadBook r")

public class ReadBook implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReadID")
    private int readID;

    @Column(name = "DateRead")
    private LocalDateTime dateRead;

    @Column(name = "Progress")
    private Float progress;

    @Column(name = "ActivityType")
    private boolean activityType;

    @ManyToOne
    @JoinColumn(name = "BookID")
    @JsonManagedReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "UserID")
     @JsonManagedReference
//    @JsonBackReference
    private User user;

}