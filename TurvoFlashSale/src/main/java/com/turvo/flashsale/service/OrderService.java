package com.turvo.flashsale.service;

import java.util.List;

import com.turvo.flashsale.bean.Account;
import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.model.CartInfo;
import com.turvo.flashsale.model.OrderDetailInfo;
import com.turvo.flashsale.model.OrderInfo;
import com.turvo.flashsale.pagination.PaginationResult;

public interface OrderService {

	Product findProduct(String code);

	PaginationResult<OrderInfo> orderList(int page, Account account);

	OrderInfo getOrderInfo(Integer orderId, Account account);

	List<OrderDetailInfo> listOrderDetailInfos(String orderId);

	CartInfo saveOrder(CartInfo cartInfo);

}
