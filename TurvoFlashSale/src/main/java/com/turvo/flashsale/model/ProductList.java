package com.turvo.flashsale.model;

import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.pagination.PaginationResult;

public class ProductList {

	private Boolean flashSale;

	PaginationResult<Product> productInfo;

	public PaginationResult<Product> getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(PaginationResult<Product> paginationResult) {
		this.productInfo = paginationResult;
	}

	public Boolean getFlashSale() {
		return flashSale;
	}

	public void setFlashSale(Boolean flashSale) {
		this.flashSale = flashSale;
	}

}
