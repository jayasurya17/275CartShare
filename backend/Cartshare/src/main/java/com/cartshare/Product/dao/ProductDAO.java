package com.cartshare.Product.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.cartshare.models.*;
import com.cartshare.repositories.*;

@Service
public class ProductDAO {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	StoreRepository storeRepository;

	public Product save(Product product) {
		return productRepository.save(product);
	}

	public Product findById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	public List<Product> findByStore(Store store) {
		return productRepository.findByStore(store);
	}

	public List<Product> findByStoreAndIsActive(Store store) {
		return productRepository.findByStoreAndIsActive(store, true);
	}

	public List<Product> findBySKU(String SKU) {
		return productRepository.findBySku(SKU);
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public List<Product> findAllByIsActive() {
		return productRepository.findAllByIsActive(true);
	}

	public Product findByStoreIdAndSKU(Store store, String SKU) {
		return productRepository.findProductByStoreAndSku(store, SKU);
	}

	public Product findByStoreIdAndSKUAndIsActive(Store store, String SKU) {
		return productRepository.findProductByStoreAndSkuAndIsActive(store, SKU, true);
	}

}