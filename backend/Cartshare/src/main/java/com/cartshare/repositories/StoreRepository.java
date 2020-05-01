package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
import com.cartshare.models.*;

public interface StoreRepository extends JpaRepository<Store, Long>{
	
    public boolean existsByUser(User user);
    public List<Store> findAll();
    public List<Store> findByUser(User user);
    public Store findByStoreName(String name);
}
