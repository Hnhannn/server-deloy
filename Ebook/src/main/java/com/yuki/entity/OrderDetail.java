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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * The persistent class for the OrderDetails database table.
 *
 */
@Data
@Entity
@Table(name = "OrderDetails")
@NamedQuery(name = "OrderDetails.findAll", query = "SELECT o FROM OrderDetail o")
public class OrderDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OrderDetailID")
	private int orderDetailID;

	@Column(name = "price")
	private long price;

	@Column(name = "quantity")
	private int quantity;

	// bi-directional many-to-one association to Book
	@ManyToOne
	@JoinColumn(name = "bookid")
//	@JsonBackReference
	@JsonManagedReference
	private Book book;

	@ManyToOne
//	@JsonManagedReference
	 @JsonBackReference
	@JoinColumn(name = "orderid")
	private Order order;
}