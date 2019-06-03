package com.turvo.flashsale.service;

import com.turvo.flashsale.bean.Account;

public interface UserService {

	Account findByName(String name);

	Boolean createAccount(Account customerInfo);

	boolean isUserExist(Account customerInfo);

}
