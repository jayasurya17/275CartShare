package com.cartshare.Store.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.cartshare.models.*;
import com.cartshare.repositories.StoreRepository;

@Service
public class StoreDAO {

	@Autowired
	StoreRepository storeRepository;

	public Store save(Store store) {
		return storeRepository.save(store);
	}

	public boolean existsByUser(User user) {
		return storeRepository.existsByUser(user);
	}


	public Store findById(Long id) {
		return storeRepository.findById(id).orElse(null);
	}

	public List<Store> findAll() {
		return storeRepository.findAll();
	}

	public List<Store> findByAdmin(User user) {
		return storeRepository.findByUser(user);
	}

}