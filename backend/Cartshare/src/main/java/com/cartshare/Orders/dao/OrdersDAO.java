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

	@Autowired
	AssociatedOrdersRepository associatedOrdersRepository;

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

	public Orders findOrdersByUserAndStatus(User user, String status) {
		return ordersRepository.findOrdersByUserAndStatus(user, status);
	}

	public OrderItems findOrderItemsByOrdersAndProduct(Orders order, Product product) {
		return orderItemsRepository.findOrderItemsByOrdersAndProduct(order, product);
	}

	public OrderItems findOrderItemsById(Long id) {
		return orderItemsRepository.findById(id).orElse(null);
	}

	public List<Orders> findOrdersWithNoPickup(Pool pool, String status, User pickupUser, Store store) {
		// return ordersRepository.findAllOrdersByPoolAndStatusAndPickupPoolerAndStore(pool, status, pickupUser, store);
		return ordersRepository.findAllOrdersByPoolAndStoreAndPickupPooler(pool, store, pickupUser);
	}

	public List<Orders> findOrdersToBePickedUp(User user) {
		// return ordersRepository.findAllOrdersByPickupPoolerAndStatus(user, "Confirmed");
		return ordersRepository.findAllOrdersByPickupPoolerAndStatusAndUser(user, "Confirmed", user);
	}

	public Orders findOrdersById(Long id) {
		return ordersRepository.findOrdersById(id);
	}

	public AssociatedOrders saveAssociatedOrders(AssociatedOrders order) {
		return associatedOrdersRepository.save(order);
	}

	public List<Orders> findAssociatedOrders(Orders o){
		List<AssociatedOrders> l = associatedOrdersRepository.findByOrder(o);
		if(l == null || l.size() == 0)	return null;
		List<Orders> r = new ArrayList<Orders>();
		for(AssociatedOrders order : l){
			r.add(order.getAssociated());
		}
		return r;
	}

}
