package com.cartshare.models;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@NotBlank
	@Column(name = "product_name")
	private String productName;
	
	@NotBlank
	@Column(name = "description")
	private String description;
	
	@NotBlank
	@Column(name = "imageurl")
	private String imageURL;
	
	@NotBlank
	@Column(name = "brand")
	private String brand;
	
	@NotBlank
	@Column(name = "sku")
	private String sku;
	

	@Column(name = "unit")
	private String unit;
	
	@NotBlank
	@Column(name = "price")
	private Double price;
	
	@JsonIgnore
	@OneToMany(mappedBy="product", fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<OrderItems> orderItems = new ArrayList<OrderItems>();
	
	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;
	
	public Product(long id, @NotBlank String productName, @NotBlank String description,
			@NotBlank String imageURL, @NotBlank String brand, @NotBlank String sku, String unit,
			@NotBlank Double price, List<OrderItems> orderItems, Store store) {
		super();
		this.id = id;
		this.productName = productName;
		this.description = description;
		this.imageURL = imageURL;
		this.brand = brand;
		this.sku = sku;
		this.unit = unit;
		this.price = price;
		this.orderItems = orderItems;
		this.store = store;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

	public List<OrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItems> orderItems) {
		this.orderItems = orderItems;
	}
	
}
