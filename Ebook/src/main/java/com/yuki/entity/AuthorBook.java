package com.yuki.entity;

import java.io.Serializable;
import java.util.Objects;

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
 * The persistent class for the AuthorBooks database table.
 * 
 */
@Data
@Entity
@Table(name = "Authorbooks")
@NamedQuery(name = "AuthorBook.findAll", query = "SELECT a FROM AuthorBook a")
public class AuthorBook implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authorbookid")
	private int authorBookID;

	@ManyToOne
	@JoinColumn(name = "authorid")
//	@JsonBackReference
	@JsonManagedReference
	private Author author;

	@ManyToOne
	@JoinColumn(name = "bookid")
	@JsonBackReference
	private Book book;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AuthorBook that = (AuthorBook) o;
		return authorBookID == that.authorBookID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authorBookID);
	}

}