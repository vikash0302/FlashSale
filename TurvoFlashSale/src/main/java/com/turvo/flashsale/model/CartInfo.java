package com.turvo.flashsale.model;

import java.util.ArrayList;
import java.util.List;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.bean.Product;

public class CartInfo {

	private Integer orderNum;

	private Account customerInfo;

	private List<CartLineInfo> cartLines = new ArrayList<CartLineInfo>();

	private List<CartLineInfo> notSavedLineItems = new ArrayList<CartLineInfo>();

	private List<CartLineInfo> notFoundLineItems = new ArrayList<CartLineInfo>();

	public CartInfo() {

	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public List<CartLineInfo> getCartLines() {
		return this.cartLines;
	}

	private CartLineInfo findLineByCode(String code) {
		for (CartLineInfo line : this.cartLines) {
			if (line.getProductInfo().getCode().equals(code)) {
				return line;
			}
		}
		return null;
	}

	public void addProduct(Product productInfo, int quantity) {
		CartLineInfo line = this.findLineByCode(productInfo.getCode());

		if (line == null) {
			line = new CartLineInfo();
			line.setQuantity(0);
			line.setProductInfo(productInfo);
			this.cartLines.add(line);
		}
		int newQuantity = line.getQuantity() + quantity;
		if (newQuantity <= 0) {
			this.cartLines.remove(line);
		} else {
			line.setQuantity(newQuantity);
		}
	}

	public void validate() {

	}

	public void updateProduct(String code, int quantity) {
		CartLineInfo line = this.findLineByCode(code);

		if (line != null) {
			if (quantity <= 0) {
				this.cartLines.remove(line);
			} else {
				line.setQuantity(quantity);
			}
		}
	}

	public boolean isEmpty() {
		return this.cartLines.isEmpty();
	}

	public int getQuantityTotal() {
		int quantity = 0;
		for (CartLineInfo line : this.cartLines) {
			quantity += line.getQuantity();
		}
		return quantity;
	}

	public double getAmountTotal() {
		double total = 0;
		for (CartLineInfo line : this.cartLines) {
			total += line.getAmount();
		}
		return total;
	}

	public void updateQuantity(CartInfo cartForm) {
		if (cartForm != null) {
			List<CartLineInfo> lines = cartForm.getCartLines();
			for (CartLineInfo line : lines) {
				this.updateProduct(line.getProductInfo().getCode(), line.getQuantity());
			}
		}

	}

	public List<CartLineInfo> getNotSavedLineItems() {
		return notSavedLineItems;
	}

	public void setNotSavedLineItems(List<CartLineInfo> notSavedLineItems) {
		this.notSavedLineItems = notSavedLineItems;
	}

	public List<CartLineInfo> getNotFoundLineItems() {
		return notFoundLineItems;
	}

	public void setNotFoundLineItems(List<CartLineInfo> notFoundLineItems) {
		this.notFoundLineItems = notFoundLineItems;
	}

	public Account getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(Account customerInfo) {
		this.customerInfo = customerInfo;
	}

	public void setCartLines(List<CartLineInfo> cartLines) {
		this.cartLines = cartLines;
	}

}