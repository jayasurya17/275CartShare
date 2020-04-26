package com.cartshare.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement
public class Message {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

    @NotNull
	@Column(name = "from_id")
    private Long fromId;
    
    @NotNull
	@Column(name = "to_id")
    private Long toId;
    
    @NotBlank
	@Column(name = "message")
    private String message;

    @NotNull
    @Column(name = "date")
    private Date date;
    
    public Message(@NotNull Long fromId, @NotNull Long toId, @NotNull String message, @NotNull Date date){
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
        this.date = date;
    }

    public Message(){
        
    }

    public void setFromId(Long fromId){
        this.fromId = fromId;
    }

    public Long getFromId(){
        return this.fromId;
    }

    public void setToId(Long toId){
        this.toId = toId;
    }

    public Long getToId(){
        return this.toId;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }

}