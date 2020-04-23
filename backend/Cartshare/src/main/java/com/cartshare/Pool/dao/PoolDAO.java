package com.cartshare.Pool.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cartshare.models.Pool;
import com.cartshare.models.User;
import com.cartshare.repositories.PoolRepository;
import com.cartshare.repositories.UserRepository;

@Repository
public class PoolDAO {
	@Autowired
    private EntityManager entityManager;
	
	@Autowired
    UserRepository userRepository;
	@Autowired
	PoolRepository poolRepository;
	
	public Pool createPool(Pool pool, Long pooler_id) {
		User pooler = userRepository.findById(pooler_id).orElse(null);
		if(pooler != null) {
			
			pool.setPooler(pooler);
			System.out.println("New Entry");
			Pool result = poolRepository.save(pool);
			System.out.println(result);
			return result;
			
		}
		return null;
	}
	
	public Pool getPool(Long poolId) {
		Pool pool = poolRepository.findById(poolId).orElse(null);
		// System.out.println(pool.getPoolMembers());
		return pool;
	}
	
	public List<Pool> getAllPools(){
		List<Pool> pools = poolRepository.findAll();
		if(pools.size() > 0) {
			return pools;
		}
		return null;
	}
	
}
