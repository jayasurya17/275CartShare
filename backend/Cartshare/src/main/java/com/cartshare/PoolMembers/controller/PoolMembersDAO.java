package com.cartshare.PoolMembers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cartshare.models.Pool;
import com.cartshare.models.PoolMembers;
import com.cartshare.models.User;
import com.cartshare.repositories.PoolMembersRepository;
import com.cartshare.repositories.PoolRepository;
import com.cartshare.repositories.UserRepository;

@Repository
public class PoolMembersDAO {
	@Autowired
    UserRepository userRepository;
	@Autowired
	PoolRepository poolRepository;
	@Autowired
	PoolMembersRepository poolMembersRepository;
	
	public PoolMembers joinRequest(Long pool_id, Long member_id, Long reference_id) {
		
		User member = userRepository.findById(member_id).orElse(null);
		User reference = userRepository.findById(reference_id).orElse(null);
		Pool pool = poolRepository.findById(pool_id).orElse(null);
		
		if(member == null || reference == null || pool == null) {
			return null;
		}
		
		/* Need to check whether member is already part of some other pool
			*****Insert Code Here*****
		*/
		PoolMembers poolMembers = new PoolMembers();
		poolMembers.setMember(member);
		poolMembers.setReference(reference);
		poolMembers.setPool(pool);
		poolMembers.setStatus("Requested");
		return poolMembersRepository.save(poolMembers);
	}
	
	public PoolMembers manageRequest(Long pool_id, Long poolMember, String status) {
		Pool pool = poolRepository.findById(pool_id).orElse(null);
		if(pool == null)
			return null;
		
		PoolMembers poolMembers = poolMembersRepository.findById(poolMember).orElse(null);
		if(poolMembers == null)
			return null;
		
		poolMembers.setStatus(status);
		return poolMembersRepository.save(poolMembers);
	}
}
