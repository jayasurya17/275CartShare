package com.cartshare.Admin.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.cartshare.models.*;
import com.cartshare.repositories.OrderItemsRepository;
import com.cartshare.repositories.OrdersRepository;
import com.cartshare.repositories.StoreRepository;

@Service
public class AdminDAO {

	@Autowired
	StoreRepository storeRepository;

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	OrderItemsRepository orderItemsRepository;

	public Store save(Store store) {
		return storeRepository.save(store);
	}

	public boolean existsByUser(User user) {
		return storeRepository.existsByUser(user);
	}


	public Store findById(String id) {
		Long storeId = Long.parseLong(id);
		return storeRepository.findById(storeId).orElse(null);
	}

	public List<Store> findAll() {
		return storeRepository.findAll();
	}

	public Store findByName(String name) {
		return storeRepository.findByStoreName(name);
	}

	public void deleteOrder(Long id) {
		ordersRepository.deleteById(id);
	}

	public void deleteOrderItem(Long id) {
		orderItemsRepository.deleteById(id);
	}

}