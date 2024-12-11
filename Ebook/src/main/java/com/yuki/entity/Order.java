package com.yuki.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;
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
 * The persistent class for the Orders database table.
 *
 */
@Data
@Entity
@Table(name = "Orders")
@NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderid")
	private int orderID;

	@Column(name = "orderdate")
	private LocalDateTime orderDate;

	@Column(name = "orderstatus")
	private String orderStatus;

	@Column(name = "totalamount")
	private BigDecimal totalAmount;

	@OneToMany(mappedBy = "order")
//	@JsonBackReference
	 @JsonManagedReference
	private List<OrderDetail> orderDetails;

	@ManyToOne
	@JoinColumn(name = "paymentmethodid")
//	@JsonBackReference
	@JsonManagedReference
	private PaymentMethod paymentMethod;

	@ManyToOne
	@JoinColumn(name = "userid")
	// @JsonManagedReference
	@JsonBackReference
	private User user;

}