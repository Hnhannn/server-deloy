package com.yuki.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * The persistent class for the Books database table.
 * 
 */
@Data
@Entity
@Table(name = "Books")
@NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b")
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookid")
	private int bookID;

	@Column(name = "bookimage")
	private String bookImage;

	@Column(name = "bookstatus")
	private boolean bookStatus;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private int price;

	@Column(name = "title")
	private String title;

	@Column(name = "postdate")
	private LocalDateTime postDate;

	// bi-directional many-to-one association to AuthorBook
	@OneToMany(mappedBy = "book")
	@JsonManagedReference
	private List<AuthorBook> authorBooks;

	// bi-directional many-to-one association to BookCategory
	@OneToMany(mappedBy = "book")
	@JsonManagedReference
	private List<BookCategory> bookCategories;

	// bi-directional many-to-one association to BookChapter
	@OneToMany(mappedBy = "book")
	@JsonManagedReference
	private List<BookChapter> bookChapters;

	// bi-directional many-to-one association to Publisher
	@ManyToOne
	@JoinColumn(name = "publisherid")
	@JsonManagedReference
	private Publisher publisher;

	// bi-directional many-to-one association to Comment
	@OneToMany(mappedBy = "book")
//	@JsonManagedReference
	@JsonBackReference
	private List<Likes> likes;

	// bi-directional many-to-one association to Review
	@OneToMany(mappedBy = "book")
//	@JsonManagedReference
	@JsonBackReference
	private List<Review> reviews;

	// bi-directional many-to-one association to ReadBook
	@OneToMany(mappedBy = "book")
	@JsonBackReference
	private List<ReadBook> readBooks;

	// bi-directional many-to-one association to OrderDetail
	@OneToMany(mappedBy = "book")
//	@JsonManagedReference
	@JsonBackReference
	private List<OrderDetail> orderDetails;

	@OneToMany(mappedBy = "book")
	@JsonManagedReference
//	@JsonBackReference
	private List<BookBookType> bookBookTypes;

	public Book(int bookID) {
		this.bookID = bookID;
	}

	// Default constructor
	public Book() {
	}

	public Optional<BookType> getBookTypeById(int bookTypeID) {
		return bookBookTypes.stream()
				.filter(bookBookType -> bookBookType.getBookTypeID() == bookTypeID)
				.map(BookBookType::getBookType)
				.findFirst();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return bookID == book.bookID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookID);
	}
}