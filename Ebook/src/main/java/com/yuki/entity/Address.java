package com.yuki.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
 * The persistent class for the Address database table.
 * 
 */
@Data
@Entity
@Table(name = "Address")
@NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "addressid")
	private int addressID;

	@Column(name = "addressline")
	private String addressLine;

	@Column(name = "cityCode")
	private int city;

	@Column(name = "districtCode")
	private int district;

	@Column(name = "phonenumber")
	private String phoneNumber;

	@Column(name = "wardCode")
	private int wardCode;

	@ManyToOne
	@JoinColumn(name = "userid")
	// @JsonManagedReference
	@JsonBackReference
	private User user;

}