package com.turvo.flashsale.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Version;
@Entity
@Table(name="sold_product")
public class SoldProduct implements Serializable{

		private static final long serialVersionUID = -1000119078147252957L;
		
		@Id
		@Column
		private String id;
		
		@Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "Create_Date", nullable = false)
		@CreationTimestamp
	    private Date createDate;
		
		@Temporal(TemporalType.DATE)
	    @Column(name = "sold_date", nullable = false)
	    private Date soldDate;
		
		
		@Column
		private int quantity;

		@Version
		private Integer version;
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public Date getSoldDate() {
			return soldDate;
		}

		public void setSoldDate(Date soldDate) {
			this.soldDate = soldDate;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}
		
		
	 
}
