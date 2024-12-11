package com.yuki.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * The persistent class for the BookChapters database table.
 * 
 */
@Data
@Entity
@Table(name = "Bookchapters")
@NamedQuery(name = "BookChapter.findAll", query = "SELECT b FROM BookChapter b")
public class BookChapter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chapterid")
	private int chapterID;

	@Column(name = "audiolink")
	private String audioLink;

	@Column(name = "chaptercontent")
	private String chapterContent;

	@Column(name = "chaptertitle")
	private String chapterTitle;

	// bi-directional many-to-one association to Book
	@ManyToOne
	@JoinColumn(name = "bookid")
	@JsonBackReference
	private Book book;

	@Override
	public String toString() {
		return "BookChapter{" +
				"chapterID=" + chapterID +
				", chapterTitle='" + chapterTitle + '\'' +
				'}';
	}
}