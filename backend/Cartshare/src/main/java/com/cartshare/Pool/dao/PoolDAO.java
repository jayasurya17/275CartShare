package com.cartshare.Pool.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cartshare.models.*;
import com.cartshare.repositories.*;

@Repository
public class PoolDAO {
	
	@Autowired
    UserRepository userRepository;
	PoolRepository poolRepository;
	
	public Pool createPool(Pool pool, Long leader_id, Long pooler_id) {
		
		User leader = userRepository.findById(leader_id).orElse(null);
		User pooler = userRepository.findById(pooler_id).orElse(null);
		System.out.println(leader);
		System.out.println(pooler);
		if(leader != null && pooler != null) {
			// pool.setLeader(leader);
			pool.setUser(pooler);
			return poolRepository.save(pool);
			
		}
		return null;
	}
	
}
