package com.cartshare.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "pool_members")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement
public class PoolMembers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "pool_id")
	private Pool pool;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private User member;
	
	@ManyToOne
	@JoinColumn(name = "reference_id")
	private User reference;
	
	@NotBlank
	@Column(name = "status")
	private String status;

	public PoolMembers(Pool pool, User member, User reference, @NotBlank String status) {
		super();
		this.pool = pool;
		this.member = member;
		this.reference = reference;
		this.status = status;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public User getMember() {
		return member;
	}

	public void setMember(User member) {
		this.member = member;
	}

	public User getReference() {
		return reference;
	}

	public void setReference(User reference) {
		this.reference = reference;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
