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

	public Pool(long id, User pooler, @NotBlank String poolName, @NotBlank String neighborhoodName, String description,
			String zipcode, Set<PoolMembers> poolMembers) {
		super();
		this.id = id;
		this.pooler = pooler;
		this.poolName = poolName;
		this.neighborhoodName = neighborhoodName;
		this.description = description;
		this.zipcode = zipcode;
		this.poolMembers = poolMembers;
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
	
}
