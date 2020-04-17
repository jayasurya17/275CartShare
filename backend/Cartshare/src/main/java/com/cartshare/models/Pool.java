package com.cartshare.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
	private User user;
	
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
	
	@ManyToOne
	@JoinColumn(name = "leader_id")
	private User leader;
	
	
	@OneToMany(mappedBy="pool", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<PoolMembers> poolMembers = new HashSet<PoolMembers>();


	public Pool(long id, User user, @NotBlank String poolName, @NotBlank String neighborhoodName, String description,
			String zipcode, User leader, Set<PoolMembers> poolMembers) {
		super();
		this.id = id;
		this.user = user;
		this.poolName = poolName;
		this.neighborhoodName = neighborhoodName;
		this.description = description;
		this.zipcode = zipcode;
		this.leader = leader;
		this.poolMembers = poolMembers;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
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


	public User getLeader() {
		return leader;
	}


	public void setLeader(User leader) {
		this.leader = leader;
	}


	public Set<PoolMembers> getPoolMembers() {
		return poolMembers;
	}


	public void setPoolMembers(Set<PoolMembers> poolMembers) {
		this.poolMembers = poolMembers;
	}
	
	
}
