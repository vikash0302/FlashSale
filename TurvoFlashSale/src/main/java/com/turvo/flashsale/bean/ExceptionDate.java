package com.turvo.flashsale.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="exception_date")
public class ExceptionDate implements Serializable{
		
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "exceptiondate", nullable = false)
    private Date exceptiondate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private Date starttime;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time", nullable = false)
    private Date endtime;
	
	@Column(name="sale_type")
	private String saleType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getExceptiondate() {
		return exceptiondate;
	}

	public void setExceptiondate(Date exceptiondate) {
		this.exceptiondate = exceptiondate;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	
	
	
}
