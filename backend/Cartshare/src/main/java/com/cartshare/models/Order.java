package com.cartshare.models;

import java.util.ArrayList;
import java.util.Date;
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
@Table(name = "order")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "pool_id")
	private Pool pool;
	
	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;
	
	@NotBlank
	@Column(name = "isfulfilled")
	private boolean isFulfilled;
	
	@NotBlank
	@Column(name = "order_type")
	private String orderType;
	
	@NotBlank
	@Column(name = "status")
	private String status;
	
	@NotBlank
	@Column(name = "timestamp")
	private Date timestamp;
	
	@OneToMany(mappedBy="order", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<OrderItems> orderItems = new HashSet<OrderItems>();

	public Order(long id, Pool pool, Store store, @NotBlank boolean isFulfilled, @NotBlank String orderType,
			@NotBlank String status, @NotBlank Date timestamp, Set<OrderItems> orderItems) {
		super();
		this.id = id;
		this.pool = pool;
		this.store = store;
		this.isFulfilled = isFulfilled;
		this.orderType = orderType;
		this.status = status;
		this.timestamp = timestamp;
		this.orderItems = orderItems;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public boolean isFulfilled() {
		return isFulfilled;
	}

	public void setFulfilled(boolean isFulfilled) {
		this.isFulfilled = isFulfilled;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Set<OrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItems> orderItems) {
		this.orderItems = orderItems;
	}
	
}
