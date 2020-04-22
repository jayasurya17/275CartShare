package com.cartshare.Orders.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.cartshare.models.*;
import com.cartshare.repositories.*;

@Service
public class OrdersDAO {

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	OrderItemsRepository orderItemsRepository;

	public List<Orders> findOrdersByUser(User user) {
		return ordersRepository.findByUser(user);
	}

	public List<OrderItems> findProductsInAOrder(Orders order) {
		return orderItemsRepository.findByOrders(order);
	}

	public Orders saveOrderDetails(Orders order) {
		return ordersRepository.save(order);
	}

	public OrderItems saveOrderItem(OrderItems orderItem) {
		return orderItemsRepository.save(orderItem);
	}

	public void removeProductFromCart(Long orderItemId) {
		orderItemsRepository.deleteById(orderItemId);
	}

	public void removeOrderFromCart(Long orderId) {
		ordersRepository.deleteById(orderId);
	}

}
