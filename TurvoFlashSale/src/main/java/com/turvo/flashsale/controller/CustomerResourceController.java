package com.turvo.flashsale.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.service.UserService;

import net.rossillo.spring.web.mvc.CacheControl;

@RestController
public class CustomerResourceController {

	public static final Logger logger = LoggerFactory.getLogger(CustomerResourceController.class);

	@Autowired
	UserService userService;

	@CacheControl(maxAge = 31556926)
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<Account> getUserByName(@RequestParam("userName") String name, HttpRequest req)
			throws Exception {
		Exception ex = new Exception();
		if (name == null || name.isEmpty()) {
			ex.addSuppressed(new Exception("Please provide user name.."));
			throw ex;
		}
		Account account = userService.findByName(name);
		if (account == null) {
			ex.addSuppressed(new Exception("User with user name " + name + " not found.."));
			throw ex;
		}
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	@ResponseBody
	public Object createUser(@RequestBody Account customerInfo, UriComponentsBuilder ucBuilder,
			BindingResult bindingResult) throws Exception {

		validate(customerInfo);
		if (userService.isUserExist(customerInfo)) {
			logger.error("Unable to create. A User with name {} already exist", customerInfo.getUserName());
			Exception ex = new Exception();
			ex.addSuppressed(new Exception(
					"Unable to create. A User with name " + customerInfo.getUserName() + " already exist."));
			throw ex;
		}
		userService.createAccount(customerInfo);
		return new ResponseEntity<String>("User Created Successfully!!", HttpStatus.CREATED);
	}

	private void validate(Account customerInfo) throws Exception {
		List<String> errors = new ArrayList<String>();
		if (customerInfo.getAddress() == null || customerInfo.getAddress().isEmpty()) {
			errors.add("User Address should not be null/empty.");
		}

		if (customerInfo.getEmail() == null || customerInfo.getEmail().isEmpty()) {
			errors.add("User Email is empty/not valid.");
		} else if (!customerInfo.getEmail().matches("[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")) {
			errors.add("User Email is not in valid format.");
		}

		if (customerInfo.getEncrytedPassword() == null || customerInfo.getEncrytedPassword().isEmpty()) {
			errors.add("Password should not be empty/null");
		}

		if (customerInfo.getPhone() == null || customerInfo.getPhone().isEmpty()) {
			errors.add("Phone number should not be empty/null");
		} else if (!customerInfo.getPhone().matches("(^$|[0-9]{10})")) {
			errors.add("Phone is not in valid format");
		}

		if (customerInfo.getUserName() == null || customerInfo.getUserName().isEmpty()) {
			errors.add("User name should not be empty/null.");
		}

		if (customerInfo.getUserRole() == null || customerInfo.getUserRole().isEmpty()) {
			errors.add("Role should not be empty.");
		}

		if (!errors.isEmpty()) {
			Exception ex = new Exception();
			for (String error : errors) {
				ex.addSuppressed(new Exception(error));
			}
			throw ex;
		}
	}
}
