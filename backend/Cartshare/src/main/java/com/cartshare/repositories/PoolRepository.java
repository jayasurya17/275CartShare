package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.cartshare.models.Pool;

public interface PoolRepository extends JpaRepository<Pool, Long> {
	public List<Pool> findAllByIsActive(Boolean isActive);
	public List<Pool> findAllByPoolName(String poolName);
	public List<Pool> findAllByPoolId(String poolId);
}
