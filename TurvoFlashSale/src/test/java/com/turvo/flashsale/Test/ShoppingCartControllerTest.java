package com.turvo.flashsale.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.model.CartInfo;
import com.turvo.flashsale.model.CartLineInfo;
import com.turvo.flashsale.model.OrderInfo;

public class ShoppingCartControllerTest extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@WithMockUser(username = "vikash", password = "vikash123", roles = "USER")
	public void getOrderByOrderId() throws Exception {

		String uri = "/user/order?orderId=8";

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		OrderInfo order = super.mapFromJson(content, OrderInfo.class);
		assertTrue(order != null && order.getOrderNum() == 8);
	}

	@Test
	@WithMockUser(username = "vikash", password = "vikash123", roles = "USER")
	public void createUser() throws Exception {
		String uri = "/user/shoppingCartConfirmation";
		CartInfo cartInfo = new CartInfo();
		List<CartLineInfo> cartLineList = new ArrayList<CartLineInfo>();

		CartLineInfo cartLine = new CartLineInfo();
		cartLine.setQuantity(1);

		Product product = new Product();
		product.setCode("102");
		cartLine.setProductInfo(product);

		cartLineList.add(cartLine);

		cartInfo.setCartLines(cartLineList);
		String inputJson = super.mapToJson(cartInfo);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
	}

}
