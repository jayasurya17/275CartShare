package com.cartshare.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pool")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement
public class Pool {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@NotBlank
	@Column(name = "pool_id")
	private String poolId;
	
	@ManyToOne
	@JoinColumn(name = "pooler_id")
	private User pooler;
	
	@NotBlank
	@Column(name = "pool_name", unique = true)
	private String poolName;
	
	@NotBlank
	@Column(name = "neighborhood_name", unique = true)
	private String neighborhoodName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "zipcode")
	private String zipcode;	


	@JsonIgnore
	@OneToMany(mappedBy="pool", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<PoolMembers> poolMembers = new HashSet<PoolMembers>();


	public Pool() {
		
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	public User getPooler() {
		return pooler;
	}

	public void setPooler(User pooler) {
		this.pooler = pooler;
	}

	public String getPoolName() {
		return poolName;
	}


	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}


	public String getNeighborhoodName() {
		return neighborhoodName;
	}


	public void setNeighborhoodName(String neighborhoodName) {
		this.neighborhoodName = neighborhoodName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getZipcode() {
		return zipcode;
	}


	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}


	public Set<PoolMembers> getPoolMembers() {
		return poolMembers;
	}


	public void setPoolMembers(Set<PoolMembers> poolMembers) {
		this.poolMembers = poolMembers;
	}

	public String getPoolId() {
		return this.poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	
}
