package com.turvo.flashsale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.bean.Role;
import com.turvo.flashsale.dao.AccountDAO;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private AccountDAO accountDAO;

	public Boolean createAccount(Account customerInfo) {
		if (isUserExist(customerInfo)) {
			return false;
		} else {
			Role role = accountDAO.getRole(customerInfo.getUserRole());
			if (role != null) {
				customerInfo.setRole(role);
				accountDAO.createAccount(customerInfo);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isUserExist(Account customerInfo) {

		return customerInfo.getUserName() != null ? accountDAO.findAccount(customerInfo.getUserName()) != null : false;
	}

	@Override
	public Account findByName(String name) {
		Account account = accountDAO.findAccount(name);
		return account;
	}

}
