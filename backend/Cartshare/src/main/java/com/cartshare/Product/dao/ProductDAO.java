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

	public List<Product> findByStore(Store store) {
		return productRepository.findByStore(store);
	}

}