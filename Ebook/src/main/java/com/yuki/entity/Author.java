package com.yuki.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * The persistent class for the Authors database table.
 * 
 */
@Data
@Entity
@Table(name = "Authors")
@NamedQuery(name = "Author.findAll", query = "SELECT a FROM Author a")
public class Author implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authorid")
	private int authorID;

	@Column(name = "authorname")
	private String authorName;

	@Column(name = "status")
	private boolean status;

	@OneToMany(mappedBy = "author")
//	@JsonManagedReference
	@JsonBackReference
	private List<AuthorBook> authorBooks;

}