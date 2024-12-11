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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * The persistent class for the Categories database table.
 * 
 */
@Data
@Entity
@Table(name = "Categories")
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryid")
	private int categoryID;

	@NotBlank(message = "Tên thể loại không được để trống.")
	@Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên thể loại chỉ được chứa chữ cái và số, không được chứa ký tự đặc biệt.")
	@Column(name = "categoryname")
	private String categoryName;

	@Column(name = "status")
	private boolean status;

	@OneToMany(mappedBy = "category")
//	@JsonManagedReference
	@JsonBackReference
	private List<BookCategory> bookCategories;

}