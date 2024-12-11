package com.yuki.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

/**
 * The persistent class for the Plans database table.
 * 
 */
@Data
@Entity
@Table(name = "Packageplans")
@NamedQuery(name = "PackagePlan.findAll", query = "SELECT p FROM PackagePlan p")
public class PackagePlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "planid")
	private int planID;

	@Column(name = "duration")
	private int duration;

	@Column(name = "planname")
	private String planName;

	@Column(name = "price")
	private Long price;

	@Column (name = "status")
	private boolean status;

	// bi-directional many-to-one association to UserSubscription
	@OneToMany(mappedBy = "packagePlan")
	@JsonBackReference
	private List<UserSubscription> userSubscriptions;

}