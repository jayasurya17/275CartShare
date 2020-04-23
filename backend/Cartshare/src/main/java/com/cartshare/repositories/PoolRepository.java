package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartshare.models.Pool;

public interface PoolRepository extends JpaRepository<Pool, Long> {
	
}
