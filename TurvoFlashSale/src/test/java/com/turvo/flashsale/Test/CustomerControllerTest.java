package com.turvo.flashsale.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.turvo.flashsale.bean.Account;

public class CustomerControllerTest extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void getUserByName() throws Exception {
		String uri = "/user?userName=vikash";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Account account = super.mapFromJson(content, Account.class);
		assertTrue(account != null && account.getUserName().equalsIgnoreCase("vikash"));
	}

	@Test
	public void createUser() throws Exception {
		String uri = "/createUser";
		Account account = new Account();
		account.setActive(true);
		account.setAddress("hyd");
		account.setEmail("xyz@gmail.com");
		account.setEncrytedPassword("vikash123");
		account.setPhone("1221231232");

		Random rnd = new Random();
		int randumNumber = 1000 + rnd.nextInt(9000);
		account.setUserName("ram" + randumNumber);
		account.setUserRole("admin");
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "User Created Successfully!!");
	}

}
