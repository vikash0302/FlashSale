package com.turvo.flashsale.service;

import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.model.ProductList;

public interface ProductService {

	Product findBySKU(String sku);

	ProductList productList(int page, String saleType);

}
