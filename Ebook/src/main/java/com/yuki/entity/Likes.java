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
import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The persistent class for the Comments database table.
 * 
 */
@Data
@Entity
@Table(name = "Likes")
@NamedQuery(name = "Likes.findAll", query = "SELECT l FROM Likes l")
public class Likes implements Serializable {
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LikeID")
	private int likeID;

	@Column(name = "LikeTime")
	private LocalDateTime likeTime;

	@ManyToOne
	@JoinColumn(name = "UserID")
	@JsonManagedReference
	private User user;

	@ManyToOne
	@JoinColumn(name = "BookID")
	@JsonManagedReference
	private Book book;
}