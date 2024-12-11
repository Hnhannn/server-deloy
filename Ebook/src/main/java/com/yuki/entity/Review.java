package com.yuki.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * The persistent class for the Reviews database table.
 * 
 */
@Data
@Entity
@Table(name = "Reviews")
@NamedQuery(name = "Review.findAll", query = "SELECT r FROM Review r")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reviewid")
	private int reviewID;

	@Column(name = "rating")
	private int rating;

	@Column(name = "reviewcontent")
	private String reviewContent;

	@Column(name = "reviewdate")
	private LocalDateTime reviewDate;

	@ManyToOne
	@JoinColumn(name = "bookid")
//	@JsonBackReference
	@JsonManagedReference
	private Book book;

	@ManyToOne
	@JoinColumn(name = "userid")
//	 @JsonBackReference
	@JsonManagedReference
	private User user;

}