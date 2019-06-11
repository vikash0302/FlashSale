package com.turvo.flashsale.model;

import java.util.Date;
import java.util.List;

import com.turvo.flashsale.bean.Account;

public class OrderInfo {

	private String id;
	private Date orderDate;
	private int orderNum;
	private double amount;

	private Account customerInfo;

	private List<OrderDetailInfo> details;

	public OrderInfo() {

	}

	// Using for Hibernate Query.
	public OrderInfo(String id, Date orderDate, int orderNum, //
			double amount,Account account) {
		this.id = id;
		this.orderDate = orderDate;
		this.orderNum = orderNum;
		this.amount = amount;
this.customerInfo = account;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}


	public Account getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(Account customerInfo) {
		this.customerInfo = customerInfo;
	}

	public List<OrderDetailInfo> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailInfo> details) {
		this.details = details;
	}

}
