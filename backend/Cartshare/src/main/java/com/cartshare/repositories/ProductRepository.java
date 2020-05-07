package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.cartshare.models.Product;
import com.cartshare.models.Store;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
    public List<Product> findByStore(Store store);
    public List<Product> findByStoreAndIsActive(Store store, Boolean isActive);
    public List<Product> findBySku(Long SKU);
    public List<Product> findAllByIsActive(Boolean isActive);
    public Product findProductByStoreAndSku(Store store, Long SKU);
    public Product findProductByStoreAndSkuAndIsActive(Store store, Long SKU, Boolean isActive);
}
