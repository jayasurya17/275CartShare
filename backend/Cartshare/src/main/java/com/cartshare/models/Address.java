package com.cartshare.models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement
public class Address {
	
//	@Column(name = "street")
	private String street;
	
//	@Column(name = "city")
	private String city;
	
//	@Column(name = "state")
	private String state;
	
//	@Column(name = "zipcode")
	private String zipcode;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
}
