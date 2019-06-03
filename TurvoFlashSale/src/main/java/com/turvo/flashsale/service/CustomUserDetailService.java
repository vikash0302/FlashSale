package com.turvo.flashsale.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.dao.AccountDAO;

@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	AccountDAO accountDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account user = accountDao.findAccount(username);
		if (user == null) {
			throw new UsernameNotFoundException("user with name " + username + " does not exists");
		}
		return new User(username, user.getEncrytedPassword(), getGrantAuthorities(user));
	}

	private Collection<? extends GrantedAuthority> getGrantAuthorities(Account user) {
		Collection<GrantedAuthority> grantAuthorities = new ArrayList<GrantedAuthority>();
		if (user.getUserRole().equals("admin")) {
			grantAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		grantAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return grantAuthorities;
	}

}
