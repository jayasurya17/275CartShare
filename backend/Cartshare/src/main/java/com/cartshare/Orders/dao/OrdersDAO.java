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
		// return
		// ordersRepository.findAllOrdersByPoolAndStatusAndPickupPoolerAndStore(pool,
		// status, pickupUser, store);
		return ordersRepository.findAllOrdersByPoolAndStoreAndPickupPooler(pool, store, pickupUser);
	}

	public List<Orders> findOrdersToBePickedUpByUser(User user) {
		// return ordersRepository.findAllOrdersByPickupPoolerAndStatus(user,
		// "Confirmed");
		return ordersRepository.findAllOrdersByPickupPoolerAndStatusAndUser(user, "Confirmed", user);
	}

	public List<Orders> findOrdersToBeDeliveredByUser(User user) {
		// return ordersRepository.findAllOrdersByPickupPoolerAndStatus(user,
		// "Confirmed");
		return ordersRepository.findAllOrdersByPickupPoolerAndStatus(user, "PickedUp");
	}

	public Orders findOrdersById(Long id) {
		return ordersRepository.findOrdersById(id);
	}

	public AssociatedOrders saveAssociatedOrders(AssociatedOrders order) {
		return associatedOrdersRepository.save(order);
	}

	public List<Orders> findAllOrdersToBePickedup() {
		return ordersRepository.findAllOrdersByStatus("Confirmed");
	}

	public List<Orders> findAssociatedOrders(Orders o) {
		List<AssociatedOrders> l = associatedOrdersRepository.findByOrder(o);
		if (l == null || l.size() == 0)
			return null;
		List<Orders> r = new ArrayList<Orders>();
		for (AssociatedOrders order : l) {
			r.add(order.getAssociated());
		}
		return r;
	}

	public List<Orders> findAllUnfulfilledOrdersByStore(Store store) {
		return ordersRepository.findAllOrdersByStoreAndIsFulfilled(store, false);
	}

	public void updateOrderStatus() {
		List<Orders> allOrders = ordersRepository.findAll();
		for (Orders order : allOrders) {
			Date orderTime = order.getTimestamp();
			Date now = new Date();

			long twoDays = (long) 172800000;
			long diffInMillies = Math.abs(now.getTime() - orderTime.getTime());
			// System.out.println(order.getStatus());
			if (diffInMillies >= twoDays && order.getStatus().compareTo("Delivered") != 0) {
				if (order.getPickupPooler() == null || order.getPickupPooler().getId() != order.getUser().getId()) {
					// System.out.println("Need to mark as cancelled");
					order.setStatus("Cancelled");
					ordersRepository.save(order);
					// associatedOrdersRepository.deleteByOrderAndAssociated(order, associated)
				}
			}
		}
	}

	public Set<Orders> getUnfulfilledOrdersForAProduct(Product product) {
		Set<Orders> orders = new HashSet<Orders>();
		List<OrderItems> allArderItems = orderItemsRepository.findByProduct(product);
		for (OrderItems orderItems: allArderItems) {
			if (orderItems.getOrders().isFulfilled() == false) {
				orders.add(orderItems.getOrders());
			}
		}
		return orders;
	}

}
