package com.cartshare.Pool.dao;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cartshare.models.*;
import com.cartshare.repositories.*;

@Repository
public class PoolDAO {
	
	@Autowired
    UserRepository userRepository;
	@Autowired
	PoolRepository poolRepository;
	@Autowired
	PoolMembersRepository poolMembersRepository;
	@PersistenceContext
	EntityManager entityManager;
	
	public Pool createPool(Pool pool, Long pooler_id) {
		User pooler = userRepository.findById(pooler_id).orElse(null);
		// User leader = userRepository.findById(leader_id).orElse(null);
				
		if(pooler == null) {
			return null;
		}
		List<PoolMembers> allRequests = poolMembersRepository.findAllByMember(pooler);
		for (PoolMembers request: allRequests) {
			if (request.getStatus().equals("Accepted")) {
				return null;
			}
		}
		
		pool.setPooler(pooler);
		// System.out.println("New Entry");
		Pool result = poolRepository.save(pool);
		// System.out.println(result);
		
		PoolMembers poolMembers = new PoolMembers();
		poolMembers.setMember(pooler);
		poolMembers.setReference(pooler);
		poolMembers.setPool(result);
		poolMembers.setStatus("Accepted");
		poolMembersRepository.save(poolMembers);
		return result;
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
	
	public Pool editPool(Pool pool, Long pooler_id) {
		User pooler = userRepository.findById(pooler_id).orElse(null);
		
		if(pooler != null) {
			// System.out.println("Updating Pool");
			pool.setPooler(pooler);
			Pool result = poolRepository.save(pool);
		// System.out.println(result);
			
			return result;
		}
		return null;
	}
	
	public Pool save(Pool pool) {
		return poolRepository.save(pool);
	}
	
}
