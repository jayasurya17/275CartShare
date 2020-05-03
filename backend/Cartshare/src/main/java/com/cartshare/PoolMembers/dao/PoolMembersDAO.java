package com.cartshare.PoolMembers.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
	
	@PersistenceContext
	EntityManager entityManager;
	
	public PoolMembers joinRequest(Long pool_id, Long member_id, Long reference_id) {
		System.out.println(pool_id + " " + member_id + " " + reference_id);
		User member = userRepository.findById(member_id).orElse(null);
		User reference = userRepository.findById(reference_id).orElse(null);
		Pool pool = poolRepository.findById(pool_id).orElse(null);
		System.out.println(member);
		System.out.println(reference);
		System.out.println(pool);
		
		if(member == null || reference == null || pool == null) {
			return null;
		}
		
		/* Need to check whether member is already part of some other pool
		*/
		Query query = entityManager.createQuery("FROM PoolMembers WHERE member_id = :member_id");
		query.setParameter("member_id", member_id);
		List results = query.getResultList();
		if(results.size() > 0)
			return null;
		
		String status = "Requested";
		if(member_id == reference_id) {
			status = "Accepted";
		} else if(pool.getPooler().getId() == reference_id) {
			status = "Approved";
		}
		PoolMembers poolMembers = new PoolMembers();
		poolMembers.setMember(member);
		poolMembers.setReference(reference);
		poolMembers.setPool(pool);
		poolMembers.setStatus(status);
		System.out.println(poolMembers.getStatus());
		return poolMembersRepository.save(poolMembers);
	}
	
	public PoolMembers manageRequest(Long pool_id, Long poolMember, String status) {
		Pool pool = poolRepository.findById(pool_id).orElse(null);
		if(pool == null)
			return null;
		
		User member = userRepository.findById(poolMember).orElse(null);
		if (member == null) {
			return null;
		}
		PoolMembers poolMembers = poolMembersRepository.findByMember(member);
		if(poolMembers == null)
			return null;
		
		poolMembers.setStatus(status);
		return poolMembersRepository.save(poolMembers);
	}
	
	public List<PoolMembers> supportedRequests(Long user_id){
		// System.out.println(user_id);

		Query query = entityManager.createQuery("FROM PoolMembers WHERE reference_id = :reference AND status = 'Accepted'");
		
		query.setParameter("reference", user_id);
		List results = query.getResultList();
		return results;
	}
	
	public List<PoolMembers> requestedRequests(String screen_name, boolean isLeader){
		
		Query query = entityManager.createQuery("FROM User WHERE screen_name = :screenname");
		query.setParameter("screenname", screen_name);
		
		List users = query.getResultList();
		if(users.size() < 0)
			return null;
		
		User user = (User) users.get(0);
		
		Long reference_id = user.getId();
		System.out.println(reference_id);
		System.out.println(user.getPools());
		
		Query query1;
		if(isLeader) {
			//AND pool_id = :poolid)
			
			Query pool = entityManager.createQuery("FROM Pool WHERE pooler_id = :poolerId");
			pool.setParameter("poolerId", reference_id);
			
			List res = pool.getResultList();
			if(!(res.size() > 0))
				return null;
			Pool poolDetails = (Pool) res.get(0);
			
			query1 = entityManager.createQuery("FROM PoolMembers WHERE reference_id = :reference OR (status = :status AND pool_id = :poolid)");
			query1.setParameter("reference", reference_id);
			query1.setParameter("status", "Approved");
			query1.setParameter("poolid", poolDetails.getId());
		} else {
			query1 = entityManager.createQuery("FROM PoolMembers WHERE reference_id = :reference AND status = :status ");
			query1.setParameter("reference", reference_id);
			query1.setParameter("status", "Requested");
		}
		List results = query1.getResultList();
		return results;
	}
	
	public List<PoolMembers> viewPoolMembers(Long pool_id){
		Query query = entityManager.createQuery("FROM PoolMembers WHERE pool_id = :poolid AND status = 'Accepted'");
		query.setParameter("poolid", pool_id);
		
		List results = query.getResultList();
		return results;
	}
	
	public PoolMembers getPoolByUserId(Long userId) {
		Query query = entityManager.createQuery("FROM PoolMembers WHERE member_id = :userid AND status = 'Accepted'");
		query.setParameter("userid", userId);
		
		PoolMembers results = (PoolMembers) query.getSingleResult();
		if(results != null)
			return results;
		return null;
	}
}
