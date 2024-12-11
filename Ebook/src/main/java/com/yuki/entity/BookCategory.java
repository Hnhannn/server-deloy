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
 * The persistent class for the BookCategories database table.
 * 
 */
@Data
@Entity
@Table(name = "Bookcategories")
@NamedQuery(name = "BookCategory.findAll", query = "SELECT b FROM BookCategory b")
public class BookCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookcategoryid")
	private int bookCategoryID;
	@ManyToOne
	@JoinColumn(name = "bookid")
	@JsonBackReference
	private Book book;

	@ManyToOne
	@JoinColumn(name = "categoryid")
//	@JsonBackReference
	@JsonManagedReference
	private Category category;

	@Override
	public int hashCode() {
		return Objects.hash(bookCategoryID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		BookCategory that = (BookCategory) obj;
		return Objects.equals(bookCategoryID, that.bookCategoryID);
	}

}