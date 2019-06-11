package com.turvo.flashsale.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.bean.Order;
import com.turvo.flashsale.bean.OrderDetail;
import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.bean.SoldProduct;
import com.turvo.flashsale.model.CartInfo;
import com.turvo.flashsale.model.CartLineInfo;
import com.turvo.flashsale.model.OrderDetailInfo;
import com.turvo.flashsale.model.OrderInfo;
import com.turvo.flashsale.pagination.PaginationResult;

@Transactional
@Repository
public class OrderDAO {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ProductDAO productDAO;

	public void saveOrUpdate(Object obj) {
		Session session = entityManager.unwrap(Session.class);
		Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(obj);
		} catch (HibernateException e) {
			transaction.rollback();
		} finally {
			transaction.commit();
			session.flush();
		}
	}

	public int getMaxOrderNum() {
		String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
		Session session = this.entityManager.unwrap(Session.class);
		Query query = session.createQuery(sql);
		Integer value = query.list() == null || query.list().isEmpty() ? null : (Integer) query.list().get(0);
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
	public CartInfo saveOrder(CartInfo cartInfo) {

		Session session = this.entityManager.unwrap(Session.class);
		List<CartLineInfo> notSaveLineItems = new ArrayList<CartLineInfo>();
		List<CartLineInfo> notFoundLineItems = new ArrayList<CartLineInfo>();
		int orderNum = 0;
		Order order = new Order();
		try {
			order.setId(UUID.randomUUID().toString());
			order.setOrderDate(new Date());
			order.setAmount(cartInfo.getAmountTotal());

			Account customerInfo = cartInfo.getCustomerInfo();
			order.setCustomerName(customerInfo.getUserName());
			order.setCustomerEmail(customerInfo.getEmail());
			order.setCustomerPhone(customerInfo.getPhone());
			order.setCustomerAddress(customerInfo.getAddress());
			order.setAccount(customerInfo);
			List<CartLineInfo> lines = cartInfo.getCartLines();
			int saveOrder = 0;
			for (CartLineInfo line : lines) {
				String code = line.getProductInfo().getCode();
				// TODO: handle exception
				ReentrantLock lock = new ReentrantLock();

				Product product = this.productDAO.findProduct(code);
				OrderDetail detail = new OrderDetail();
				detail.setId(UUID.randomUUID().toString());
				detail.setOrder(order);
				detail.setAmount(product.getPrice() * line.getQuantity());
				detail.setPrice(product.getPrice());

				if (product != null) {
					line.setProductInfo(product);

					detail.setProduct(product);

					SoldProduct soldProduct = productDAO.findSoldQuantity(new Date(), code);
					int soldQuantity = 0;
					if (soldProduct != null) {
						soldQuantity = soldProduct.getQuantity();
					}
					int qunatityCanPurchased = product.getCapacity() - soldQuantity;
					boolean productToSave = true;
					if (qunatityCanPurchased == 0 || qunatityCanPurchased < 0) {
						productToSave = false;
						notSaveLineItems.add(line);
					} else if (qunatityCanPurchased < line.getQuantity()) {
						line.setQuantity(qunatityCanPurchased);
						product.setSoldOut(true);

					}

					if (productToSave) {
						try {

							lock.lock();
							session.update(product);
							if (saveOrder == 0) {
								orderNum = this.getMaxOrderNum() + 1;
								order.setOrderNum(orderNum);
								session.persist(order);
								cartInfo.setOrderNum(orderNum);
								saveOrder++;
							}
							detail.setQuanity(line.getQuantity());
							session.persist(detail);
							if (soldProduct != null) {
								soldProduct.setQuantity(soldProduct.getQuantity() + line.getQuantity());
							} else {
								soldProduct = new SoldProduct();
								soldProduct.setId(code);
								soldProduct.setQuantity(line.getQuantity());
								soldProduct.setSoldDate(new Date());
							}
							session.persist(soldProduct);
						} finally {
							lock.unlock();
						}
					} else {
						notFoundLineItems.add(line);
					}
				}
			}

			if (!notFoundLineItems.isEmpty()) {
				cartInfo.setNotFoundLineItems(notFoundLineItems);
			}
			if (!notSaveLineItems.isEmpty()) {
				cartInfo.setNotSavedLineItems(notSaveLineItems);
			}
		} catch (Exception ex) {
			ex.addSuppressed(new Exception("Order confirmation failed due to data integrity.."));
			throw ex;
		}
		return cartInfo;
	}

	// @page = 1, 2, ...
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage, String userName) {
		String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
				+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
				+ Order.class.getName() + " ord where ord.customerName=:userName " + " order by ord.orderNum desc";

		Session session = this.entityManager.unwrap(Session.class);
		Query query = session.createQuery(sql);
		query.setParameter("userName", userName);
		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	public Order findOrder(Integer orderId, Account account) {
		Session session = this.entityManager.unwrap(Session.class);
		String userName = account.getUserName();
		String sql = "Select o from " + Order.class.getName()
				+ " o where o.orderNum=:orderNum and o.customerName=:userName";
		Query query = session.createQuery(sql);
		query.setParameter("orderNum", orderId);
		query.setParameter("userName", userName);
		return query.list() == null || query.list().isEmpty() ? null : (Order) query.list().get(0);
	}

	public OrderInfo getOrderInfo(Integer orderId, Account account) {
		Order order = this.findOrder(orderId, account);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getId(), order.getOrderDate(), //
				order.getOrderNum(), order.getAmount(), order.getAccount());

	}

	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
		String sql = "Select new " + OrderDetailInfo.class.getName() //
				+ "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) "//
				+ " from " + OrderDetail.class.getName() + " d "//
				+ " where d.order.id = :orderId ";

		Session session = this.entityManager.unwrap(Session.class);
		org.hibernate.Query query = session.createQuery(sql);
		query.setParameter("orderId", orderId);

		return query.list();
	}

}
