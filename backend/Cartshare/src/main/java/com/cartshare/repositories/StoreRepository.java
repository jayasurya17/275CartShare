package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
import com.cartshare.models.*;

public interface StoreRepository extends JpaRepository<Store, Long>{
	
    public boolean existsByUser(User user);
    public List<Store> findAll();
    public List<Store> findAllByIsActive(Boolean isActive);
    public List<Store> findByUser(User user);
    public Store findByStoreName(String name); 
    public List<Store> findAllByStoreName(String name);
    public List<Store> findAllByStoreNameAndIsActive(String name, Boolean isActive);
}
