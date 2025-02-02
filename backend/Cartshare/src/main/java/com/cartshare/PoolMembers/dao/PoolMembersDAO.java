package com.cartshare.PoolMembers.dao;

import java.util.List;
import java.util.Set;

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
import com.cartshare.utils.MailController;

@Repository
public class PoolMembersDAO {
	@Autowired
    UserRepository userRepository;
	@Autowired
	PoolRepository poolRepository;
	@Autowired
	PoolMembersRepository poolMembersRepository;

	@Autowired
	MailController mailController;
	
	@PersistenceContext
	EntityManager entityManager;

	public PoolMembers save(PoolMembers member) {
		return poolMembersRepository.save(member);
	}
	
	public PoolMembers joinRequest(Long pool_id, Long member_id, Long reference_id) {
		User member = userRepository.findById(member_id).orElse(null);
		User reference = userRepository.findById(reference_id).orElse(null);
		Pool pool = poolRepository.findById(pool_id).orElse(null);
		String status;
		
		if(member == null || reference == null || pool == null) {
			return null;
		}
		
		List<PoolMembers> allRequests = poolMembersRepository.findAllByMember(member);
		for (PoolMembers request: allRequests) {
			if (request.getStatus().equals("Accepted")) {
				return null;
			} else if (request.getReference().getId() == reference_id) {
				if (request.getStatus().equals("Rejected")) {

					if(pool.getPooler().getId() == reference_id) {
						status = "Approved";
						mailController.send(reference.getEmail(), "A new request to join the pool", member.getScreenName() + " has given you as reference to join your pool. Please login to accept or reject the request");
					} else {
						status = "Requested";
						mailController.send(reference.getEmail(), "A new request to join the pool", member.getScreenName() + " has given you as reference to join your pool. Please login to support or reject the request");
					}
					request.setStatus(status);
					return poolMembersRepository.save(request);

				} else {
					return request;
				}
			}
		}
		
		Set<PoolMembers> pools = reference.getPoolMembers();
		if (pools.size() == 0) {
			return null;
		} else {
            Boolean isFound = false;
            for (PoolMembers temp: pools) {
                if (temp.getStatus().equals("Accepted") && temp.getPool().getId() == pool.getId()) {
					isFound = true;
					break;
				} else if (temp.getMember().getId() == member.getId() && temp.getPool().getId() == pool.getId() && temp.getReference().getId() == reference.getId()) {
					return null;
				}
            }
            if (isFound == false) {
				return null;
            }
		}
		if(pool.getPooler().getId() == reference_id) {
			status = "Approved";
			mailController.send(reference.getEmail(), "A new request to join the pool", member.getScreenName() + " has given you as reference to join your pool. Please login to accept or reject the request");
		} else {
			status = "Requested";
			mailController.send(reference.getEmail(), "A new request to join the pool", member.getScreenName() + " has given you as reference to join your pool. Please login to support or reject the request");
		}
		PoolMembers poolMembers = new PoolMembers();
		poolMembers.setMember(member);
		poolMembers.setReference(reference);
		poolMembers.setPool(pool);
		poolMembers.setStatus(status);
		return poolMembersRepository.save(poolMembers);
	}
	
	public PoolMembers manageRequest(Long pool_id, Long poolMember, String status, Long requestId) {
		Pool pool = poolRepository.findById(pool_id).orElse(null);
		if(pool == null) {
			return null;
		}
		
		User member = userRepository.findById(poolMember).orElse(null);
		if (member == null) {
			return null;
		}

		PoolMembers poolMembers = poolMembersRepository.findById(requestId).orElse(null);
		if(poolMembers == null){
			return null;
		}

		List<PoolMembers> requestList = poolMembersRepository.findAllByMemberAndStatus(member, "Accepted");
		if (requestList != null && requestList.size() > 0) {
			poolMembers.setStatus("Rejected");
			poolMembersRepository.save(poolMembers);
			return null;
		}

		if (status.compareTo("Rejected") == 0) {
			poolMembers.setStatus(status);
			poolMembers = poolMembersRepository.save(poolMembers);
		} else if (status.compareTo("Approved") == 0) {
			List<PoolMembers> allRequests = poolMembersRepository.findAllByPoolAndMemberAndStatus(pool, member, "Approved");
			if (allRequests.size() > 0) {
				poolMembers.setStatus("Rejected");
			} else {
				poolMembers.setStatus("Approved");
			}
			poolMembers = poolMembersRepository.save(poolMembers);
		} else {
			List<PoolMembers> allRequests = poolMembersRepository.findAllByMember(member);
			for (PoolMembers memberObj: allRequests) {
				if (poolMembers.getId() != memberObj.getId()) {
					memberObj.setStatus("Rejected");
					poolMembersRepository.save(memberObj);
				}
			}
			poolMembers.setStatus("Accepted");
			poolMembers = poolMembersRepository.save(poolMembers);
		}

		return poolMembers;	
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
		Pool poolDetails = null;
		for (PoolMembers poolMembers: user.getPoolMembers()) {
			if (poolMembers.getStatus().equals("Accepted")) {
				poolDetails = poolMembers.getPool();
				break;
			}
		}
		if (poolDetails == null) {
			return null;
		} 
		Query query1;
		if(isLeader) {			
			query1 = entityManager.createQuery("FROM PoolMembers WHERE status = :status AND pool_id = :poolid");
			query1.setParameter("status", "Approved");
			query1.setParameter("poolid", poolDetails.getId());
		} else {
			query1 = entityManager.createQuery("FROM PoolMembers WHERE reference_id = :reference AND status = :status AND pool_id = :poolid");
			query1.setParameter("reference", reference_id);
			query1.setParameter("status", "Requested");
			query1.setParameter("poolid", poolDetails.getId());

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
	
	public List<PoolMembers> getPoolByUserId(Long userId) {
		Query query = entityManager.createQuery("FROM PoolMembers WHERE member_id = :userid AND status = 'Accepted'");
		query.setParameter("userid", userId);
		
		List results = query.getResultList();
		if(results.size() > 0)
			return results;
		return null;
	}

	public void deleteById(Long id) {
		poolMembersRepository.deleteById(id);
	}
}
