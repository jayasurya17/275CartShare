package com.cartshare.PoolMembers.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cartshare.PoolMembers.controller.PoolMembersDAO;
import com.cartshare.models.PoolMembers;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/poolMembers")
public class PoolMembersController {
	
	@Autowired
	PoolMembersDAO poolMembersDAO;
	
	@PostMapping(value = "/joinPool", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> joinPool(@RequestParam(required = true) String poolId,
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String referenceId){
		try {
			if(poolId == null || userId == null || referenceId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}
			poolId = poolId.trim();
			userId = userId.trim();
			referenceId = referenceId.trim();
			
			Long pool_id = Long.parseLong(poolId);
			Long member_id = Long.parseLong(userId);
			Long reference_id = Long.parseLong(referenceId);
			System.out.println(pool_id + " " + member_id + " " + reference_id);
			PoolMembers poolMembers = poolMembersDAO.joinRequest(pool_id, member_id, reference_id);
			
			if(poolMembers == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Details");
			}
			System.out.println(poolMembers);
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PutMapping(value = "/manageRequest", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> acceptRequest(@RequestParam(required = true) String poolId,
			@RequestParam(required = true) String poolMemberId,
			@RequestParam(required = true) String status){
		try {
			if(poolId == null || poolMemberId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}
			status = status.trim();
			if(status != "Accepted" && status != "Rejected")
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
			poolId = poolId.trim();
			poolMemberId = poolMemberId.trim();
			
			Long pool_id = Long.parseLong(poolId);
			Long poolMember = Long.parseLong(poolMemberId);
			
			PoolMembers poolMembers = poolMembersDAO.manageRequest(pool_id, poolMember, status);
			if(poolMembers == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Details");
			}
			System.out.println(poolMembers);
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
			
			
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
}
