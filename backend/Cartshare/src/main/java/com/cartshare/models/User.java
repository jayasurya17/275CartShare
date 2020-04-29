package com.cartshare.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@Column(name = "isactive")
	private boolean isActive;

	@NotNull
	@Column(name = "isverified")
	private boolean isVerified;

	@NotNull
	@Column(name = "isprofilecomplete")
	private boolean isProfileComplete;

	@NotBlank
	@Column(name = "verification_code")
	private String verificationCode;

	@NotNull
	@Column(name = "contributioncredit")
	private Long contributionCredit;
	
	@Embedded 
	@AttributeOverrides(
			value = {
					@AttributeOverride(name = "street", column = @Column(name = "street")),
					@AttributeOverride(name = "city", column = @Column(name = "city")),
					@AttributeOverride(name = "state", column = @Column(name = "state")),
					@AttributeOverride(name = "zipcode", column = @Column(name = "zipcode"))
			}
	)
	private Address address;

	@JsonIgnore
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Store> stores = new HashSet<Store>();

	@JsonIgnore
	@OneToMany(mappedBy="pooler", fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Pool> pools = new HashSet<Pool>();

	@JsonIgnore
	@OneToMany(mappedBy="member", fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<PoolMembers> poolMembers = new HashSet<PoolMembers>();

	@JsonIgnore
	@OneToMany(mappedBy="reference", fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<PoolMembers> refernces = new HashSet<PoolMembers>();

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Orders> orders = new HashSet<Orders>();

	@JsonIgnore
	@OneToMany(mappedBy = "pickupPooler", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Orders> ordersToPickUp = new HashSet<Orders>();

	public User(@NotBlank String uid, @NotBlank String email, @NotBlank String nickName, @NotBlank String screenName,
			@NotBlank boolean isAdmin, @NotBlank boolean isVerified, @NotBlank boolean isActive,
			@NotBlank boolean isProfileComplete, @NotBlank String verificationCode, Set<Store> stores, Set<Pool> pools,
			Set<PoolMembers> poolMembers, Set<PoolMembers> refernces) {
		super();
		this.uid = uid;
		this.email = email;
		this.nickName = nickName;
		this.screenName = screenName;
		this.isAdmin = isAdmin;
		this.isActive = isActive;
		this.isVerified = isVerified;
		this.stores = stores;
		this.pools = pools;
		this.poolMembers = poolMembers;
		this.refernces = refernces;
		this.isProfileComplete = isProfileComplete;
		this.verificationCode = verificationCode;
		this.contributionCredit = (long) 0;
	}

	public User() {

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

	public String getVerificationCode() {
		return this.verificationCode;
	}

	public void setVerificationCode(String code) {
		this.verificationCode = code;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isVerified() {
		return this.isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public boolean isProfileComplete() {
		return this.isProfileComplete;
	}

	public void setProfileComplete(boolean isProfileComplete) {
		this.isProfileComplete = isProfileComplete;
	}

	public Set<Store> getStores() {
		return stores;
	}

	public void setStores(Set<Store> stores) {
		this.stores = stores;
	}

	public Set<Pool> getPools() {
		return pools;
	}

	public void setPools(Set<Pool> pools) {
		this.pools = pools;
	}

	public Set<PoolMembers> getPoolMembers() {
		return poolMembers;
	}

	public void setPoolMembers(Set<PoolMembers> poolMembers) {
		this.poolMembers = poolMembers;
	}

	public Set<PoolMembers> getRefernces() {
		return refernces;
	}

	public void setRefernces(Set<PoolMembers> refernces) {
		this.refernces = refernces;
	}

	public Long getContributionCredit() {
		return this.contributionCredit;
	}

	public void setContributionCredit(Long contributionCredit) {
		this.contributionCredit = contributionCredit;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Orders> getOrders() {
		return orders;
	}

	public void setOrders(Set<Orders> orders) {
		this.orders = orders;
	}

	public Set<Orders> getOrdersToPickUp() {
		return this.ordersToPickUp;
	}

	public void setOrdersToPickUp(Set<Orders> ordersToPickUp) {
		this.ordersToPickUp = ordersToPickUp;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
