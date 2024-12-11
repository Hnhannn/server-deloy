package com.yuki.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * The persistent class for the UserSubscriptions database table.
 * 
 */
@Data
@Entity
@Table(name = "Usersubscriptions")
@NamedQuery(name = "UserSubscription.findAll", query = "SELECT u FROM UserSubscription u")
public class UserSubscription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usersubscriptionid")
	private int userSubscriptionID;

	@Column(name = "enddate")
	private LocalDateTime endDate;

	@Column(name = "startdate")
	private LocalDateTime startDate;

	@Column(name = "status")
	private String status;

	@Column(name = "subscriptiondate")
	private LocalDateTime subscriptionDate;

	@ManyToOne
	@JoinColumn(name = "PlanID")
	@JsonManagedReference
	private PackagePlan packagePlan;

		@ManyToOne
		@JoinColumn(name = "userid")
		 @JsonBackReference
		private User user;

}