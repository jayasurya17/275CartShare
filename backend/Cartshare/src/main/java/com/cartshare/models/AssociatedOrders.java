package com.cartshare.models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "associated_orders")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement
public class AssociatedOrders {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders order;

	
	@ManyToOne
	@JoinColumn(name = "associated_id")
    private Orders associated;

	public AssociatedOrders() {
        
	}

	public AssociatedOrders(Orders order, Orders associated) {
        this.order = order;
        this.associated = associated;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
    }
    
    public Orders getOrder() {
        return this.order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Orders getAssociated() {
        return this.associated;
    }

    public void setAssociated(Orders associated) {
        this.associated = associated;
    }
    
	
}
