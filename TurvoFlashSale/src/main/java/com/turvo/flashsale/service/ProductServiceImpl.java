package com.turvo.flashsale.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.bean.SoldProduct;
import com.turvo.flashsale.dao.ProductDAO;
import com.turvo.flashsale.model.ProductList;
import com.turvo.flashsale.pagination.PaginationResult;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDAO productDao;

	@Override
	public Product findBySKU(String sku) {

		return productDao.findProduct(sku);
	}

	@Override
	public ProductList productList(int page, String saleType) {
		PaginationResult<Product> paginationResult = productDao.queryProducts(page, 1, 5, saleType);
		ProductList productlist = new ProductList();
		Boolean isExceptionDate = productDao.findIfExceptionDate(new Date());

		productlist.setFlashSale(isExceptionDate != null ? isExceptionDate : false);// implement based on exception
																					// dates..
		if (paginationResult != null && !paginationResult.getList().isEmpty()) {
			List<Product> list = paginationResult.getList();
			List<Product> newList = new ArrayList<Product>();
			for (Product productInfo : list) {
				SoldProduct soldProducts = productDao.findSoldQuantity(new Date(), productInfo.getCode());
				if ((soldProducts != null && productInfo.getCapacity() >= soldProducts.getQuantity())
						|| productInfo.getCapacity() == 0) {
					productInfo.setSoldOut(true);
				}
				newList.add(productInfo);
			}
			paginationResult.setList(newList);
		}

		productlist.setProductInfo(paginationResult);
		return productlist;
	}

}
