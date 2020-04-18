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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement

public class User {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@NotBlank
	@Column(name = "uid")
	private String uid;
	
	@NotBlank
	@Column(name = "email")
	private String email;
	
	@NotBlank
	@Column(name = "nick_name")
	private String nickName;
	
	@NotBlank
	@Column(name = "screen_name")
	private String screenName;
	
	@NotNull
	@Column(name = "isadmin")
	private boolean isAdmin;
	
	@NotNull
	@Column(name = "isverified")
	private boolean isVerified;
	
	@NotNull
	@Column(name = "isactive")
	private boolean isActive;
	
	@NotNull
	@Column(name = "isprofilecomplete")
	private boolean isProfileComplete;
	
	// @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
	// @LazyCollection(LazyCollectionOption.FALSE)
	// private Set<Store> stores = new HashSet<Store>();
	
	// @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
	// @LazyCollection(LazyCollectionOption.FALSE)
	// private Set<Pool> pools = new HashSet<Pool>();
	
	// @OneToMany(mappedBy="leader", fetch = FetchType.EAGER)
	// @LazyCollection(LazyCollectionOption.FALSE)
	// private Set<Pool> leaders = new HashSet<Pool>();
	
	// @OneToMany(mappedBy="member", fetch = FetchType.EAGER)
	// @LazyCollection(LazyCollectionOption.FALSE)
	// private Set<PoolMembers> poolMembers = new HashSet<PoolMembers>();
	
	// @OneToMany(mappedBy="reference", fetch = FetchType.EAGER)
	// @LazyCollection(LazyCollectionOption.FALSE)
	// private Set<PoolMembers> refernces = new HashSet<PoolMembers>();
	
	
	public User(@NotBlank String uid, @NotBlank String email, @NotBlank String nickName, @NotBlank String screenName,
			@NotBlank boolean isAdmin, @NotBlank boolean isVerified, @NotBlank boolean isActive,
			@NotBlank boolean isProfileComplete, Set<Store> stores, Set<Pool> pools, Set<Pool> leaders,
			Set<PoolMembers> poolMembers, Set<PoolMembers> refernces) {
		super();
		this.uid = uid;
		this.email = email;
		this.nickName = nickName;
		this.screenName = screenName;
		this.isAdmin = isAdmin;
		this.isVerified = isVerified;
		this.isActive = isActive;
		this.isProfileComplete = isProfileComplete;
		// this.stores = stores;
		// this.pools = pools;
		// this.leaders = leaders;
		// this.poolMembers = poolMembers;
		// this.refernces = refernces;
	}
	public User(){

	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isProfileComplete() {
		return isProfileComplete;
	}
	public void setProfileComplete(boolean isProfileComplete) {
		this.isProfileComplete = isProfileComplete;
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	// public Set<Store> getStores() {
	// 	return stores;
	// }
	// public void setStores(Set<Store> stores) {
	// 	this.stores = stores;
	// }
	// public Set<Pool> getPools() {
	// 	return pools;
	// }
	// public void setPools(Set<Pool> pools) {
	// 	this.pools = pools;
	// }
	// public Set<Pool> getLeaders() {
	// 	return leaders;
	// }
	// public void setLeaders(Set<Pool> leaders) {
	// 	this.leaders = leaders;
	// }
	// public Set<PoolMembers> getPoolMembers() {
	// 	return poolMembers;
	// }
	// public void setPoolMembers(Set<PoolMembers> poolMembers) {
	// 	this.poolMembers = poolMembers;
	// }
	// public Set<PoolMembers> getRefernces() {
	// 	return refernces;
	// }
	// public void setRefernces(Set<PoolMembers> refernces) {
	// 	this.refernces = refernces;
	// }
	
}
