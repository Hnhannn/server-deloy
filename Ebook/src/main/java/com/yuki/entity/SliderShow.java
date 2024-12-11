package com.yuki.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "SliderShow")
@Data
@NoArgsConstructor
public class SliderShow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SliderID")
    private int sliderID;

    @Column(name = "ImgageUrl")
    private String imageUrl;

    @Column(name = "Status")
    private boolean status;

    @Column(name = "`Current`")
    private int current;

    // Define relationships if any
    // Example: Many-to-One relationship with User entity
    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnore
    private User user;
}