package com.turvo.flashsale.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.model.ProductList;
import com.turvo.flashsale.service.ProductService;

import net.rossillo.spring.web.mvc.CacheControl;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@CacheControl(maxAge = 31556926)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Product> findByIdentifier(@RequestParam("sku") String sku) throws Exception {
		Product product = productService.findBySKU(sku);
		Exception ex = new Exception();
		if (product == null) {
			ex.addSuppressed(new Exception("Product with code: " + sku + " not found.."));
			throw ex;
		} else {
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		}
	}

	@RequestMapping(value = { "/productList" }, method = RequestMethod.GET)
	public ResponseEntity<ProductList> productListing(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "1") String pageStr,
			@RequestParam(value = "saleType", defaultValue = "normal") String saleType) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
			e.addSuppressed(new Exception("Input parameter are not valid.."));
			throw e;
		}

		return new ResponseEntity<ProductList>(productService.productList(page, saleType), HttpStatus.OK);
	}
}
