package com.turvo.flashsale.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.model.CartInfo;
import com.turvo.flashsale.model.CartLineInfo;
import com.turvo.flashsale.model.OrderDetailInfo;
import com.turvo.flashsale.model.OrderInfo;
import com.turvo.flashsale.pagination.PaginationResult;
import com.turvo.flashsale.service.OrderService;
import com.turvo.flashsale.service.ProductService;
import com.turvo.flashsale.service.UserService;

import net.rossillo.spring.web.mvc.CacheControl;

@RestController
@RequestMapping("/user")
public class ShoppingCartController {

	@Autowired
	private OrderService orderService;

	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;

	@CacheControl(maxAge = 31556926)
	@RequestMapping(value = { "/orderList" }, method = RequestMethod.GET)
	public PaginationResult<OrderInfo> orderList(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "1") String pageStr) throws Exception {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);

		} catch (Exception e) {
			e.addSuppressed(new Exception("Please provide valid/numeric page number.."));
			throw e;
		}

		return orderService.orderList(page, GetCurrentLoginUser());
	}

	@CacheControl(maxAge = 31556926)
	@RequestMapping(value = { "/order" }, method = RequestMethod.GET)
	public ResponseEntity<OrderInfo> orderView(HttpServletRequest request, @RequestParam("orderId") Integer orderId)
			throws Exception {
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = this.orderService.getOrderInfo(orderId, GetCurrentLoginUser());
		}
		if (orderInfo == null) {
			String error = "Order with orderId " + orderId + " not found..";
			Exception ex = new Exception();
			ex.addSuppressed(new Exception(error));
			throw ex;
		}
		List<OrderDetailInfo> details = this.orderService.listOrderDetailInfos(orderInfo.getId());
		orderInfo.setDetails(details);

		return new ResponseEntity<OrderInfo>(orderInfo, HttpStatus.OK);
	}

	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
	public ResponseEntity<CartInfo> shoppingCartConfirmationSave(@RequestBody CartInfo cartInfo,
			HttpServletRequest request) throws Exception {

		validate(cartInfo);
		try {
			cartInfo.setCustomerInfo(GetCurrentLoginUser());
			cartInfo = orderService.saveOrder(cartInfo);
		} catch (Exception e) {
			e.printStackTrace();
			e.addSuppressed(new Exception("Order not confirmed: " + e.getMessage()));
			throw e;
		}

		return new ResponseEntity<CartInfo>(cartInfo, HttpStatus.OK);
	}

	private void validate(CartInfo cartInfo) throws Exception {
		List<String> errors = new ArrayList<>();
		if (cartInfo.isEmpty()) {
			errors.add("No Content Found");
		}

		if (cartInfo.getCartLines() == null || cartInfo.getCartLines().isEmpty()) {
			errors.add("No Product line item found..");
		} else {
			List<CartLineInfo> cartlines = cartInfo.getCartLines();
			int itemNo = 0;
			for (CartLineInfo cartLine : cartlines) {
				if (cartLine.getQuantity() <= 0) {
					errors.add("Qunatity either not exist or less than equal to 0 for line item: " + itemNo);
				}
				if (cartLine.getProductInfo().getCode() == null || cartLine.getProductInfo().getCode().isEmpty()) {
					errors.add("Poduct Code is missing for line item: " + itemNo);
				}
				itemNo++;
			}

		}
		if (!errors.isEmpty()) {
			Exception ex = new Exception();
			for (String error : errors) {
				ex.addSuppressed(new Exception(error));
			}
			throw ex;
		}
	}

	private Account GetCurrentLoginUser() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return userService.findByName(currentUserName);
		}
		throw new Exception();
	}

}
