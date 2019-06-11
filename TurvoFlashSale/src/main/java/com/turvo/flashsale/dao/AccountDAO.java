package com.turvo.flashsale.dao;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.bean.Role;

@Transactional
@Repository
public class AccountDAO {

	@Autowired
	private EntityManager entityManager;

	public Account findAccount(String userName) {
		Session session = this.entityManager.unwrap(Session.class);
		return session.get(Account.class, userName);
	}

	public void createAccount(Account account) {
		Session session = this.entityManager.unwrap(Session.class);
		session.persist(account);
		// Flush
		session.flush();
	}
	
	public Role getRole(String userRole) {
		Session session = this.entityManager.unwrap(Session.class);
		Query query  = session.createQuery("Select e from "+Role.class.getName()+" as e where e.name =:userRole");
		query.setParameter("userRole", userRole);
		return query.list()==null || query.list().isEmpty() ? null : (Role)query.list().get(0);
	}

}