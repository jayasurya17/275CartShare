package com.cartshare.Store.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.*;
import com.cartshare.models.*;
import com.cartshare.repositories.StoreRepository;

@Service
@Repository
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

	public List<Store> findAllByIsActive() {
		return storeRepository.findAllByIsActive(true);
	}

	public List<Store> findByAdmin(User user) {
		return storeRepository.findByUser(user);
	}

	public List<Store> findAllByStoreName(String storeName) {
		 return storeRepository.findAllByStoreName(storeName);
	}

	public List<Store> findAllByStoreNameAndIsActive(String storeName) {
		 return storeRepository.findAllByStoreNameAndIsActive(storeName, true);
	}
}