package com.cartshare.models;

import javax.persistence.AttributeOverrides;
import javax.persistence.Column;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Embedded;
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
@Table(name = "store")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement
public class Store {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@NotBlank
	@Column(name = "store_name")
	private String storeName;
	
	@NotBlank
	@Column(name = "isactive")
	private boolean isActive;
	
	@Embedded 
	@AttributeOverrides(
			value = {
					@AttributeOverride(name = "street", column = @Column(name = "street")),
					@AttributeOverride(name = "city", column = @Column(name = "city")),
					@AttributeOverride(name = "state", column = @Column(name = "state")),
					@AttributeOverride(name = "zip", column = @Column(name = "zipcode"))
			}
	)
	private Address address;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy="store", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<StoreItems> storeItems = new HashSet<StoreItems>();
	
	@OneToMany(mappedBy="store", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Order> orders = new HashSet<Order>();
	
	
	public Store(long id, @NotBlank String storeName, @NotBlank boolean isActive, Address address, User user,
			Set<StoreItems> storeItems, Set<Order> orders) {
		super();
		this.id = id;
		this.storeName = storeName;
		this.isActive = isActive;
		this.address = address;
		this.user = user;
		this.storeItems = storeItems;
		this.orders = orders;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<StoreItems> getStoreItems() {
		return storeItems;
	}

	public void setStoreItems(Set<StoreItems> storeItems) {
		this.storeItems = storeItems;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
}
