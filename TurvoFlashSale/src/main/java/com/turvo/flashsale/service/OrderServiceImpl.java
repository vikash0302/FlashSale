package com.turvo.flashsale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.dao.OrderDAO;
import com.turvo.flashsale.dao.ProductDAO;
import com.turvo.flashsale.model.CartInfo;
import com.turvo.flashsale.model.OrderDetailInfo;
import com.turvo.flashsale.model.OrderInfo;
import com.turvo.flashsale.pagination.PaginationResult;
import com.turvo.flashsale.util.OrderConfirmationException;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	ProductDAO productDAO;

	@Autowired
	OrderDAO orderDAO;

	@Override
	public Product findProduct(String code) {

		return productDAO.findProduct(code);
	}

	@Override
	public PaginationResult<OrderInfo> orderList(int page, Account account) {
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;

		PaginationResult<OrderInfo> paginationResult //
				= orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE, account.getUserName());
		if (paginationResult != null && !paginationResult.getList().isEmpty()) {
			List<OrderInfo> list = paginationResult.getList();
			List<OrderInfo> newList = new ArrayList<OrderInfo>();
			for (OrderInfo orderInfo : list) {
				orderInfo.setDetails(orderDAO.listOrderDetailInfos(orderInfo.getId()));
				newList.add(orderInfo);
			}
			paginationResult.setList(newList);
		}
		return paginationResult;
	}

	@Override
	public OrderInfo getOrderInfo(Integer orderId, Account account) {
		return orderDAO.getOrderInfo(orderId, account);
	}

	@Override
	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
		return orderDAO.listOrderDetailInfos(orderId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = OrderConfirmationException.class)
	public CartInfo saveOrder(CartInfo cartInfo) {

		return orderDAO.saveOrder(cartInfo);
	}

}
