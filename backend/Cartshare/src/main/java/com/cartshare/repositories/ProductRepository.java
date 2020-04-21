package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.cartshare.models.*;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
    public List<Product> findByStore(Store store);
    public List<Product> findBySku(String SKU);
}
