package com.yuki.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * The persistent class for the Publishers database table.
 * 
 */
@Data
@Entity
@Table(name = "Publishers")
@NamedQuery(name = "Publisher.findAll", query = "SELECT p FROM Publisher p")
public class Publisher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "publisherid")
	private int publisherID;

	@NotBlank(message = "Tên nhà xuất bản không được để trống.")
	@Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên nhà xuất bản chỉ được chứa chữ cái và số, không được chứa ký tự đặc biệt.")
	@Column(name = "publishername")
	private String publisherName;

	@Column(name = "status")
	private boolean status;

	// bi-directional many-to-one association to Book
	@OneToMany(mappedBy = "publisher")
	@JsonBackReference
	private List<Book> books;



}