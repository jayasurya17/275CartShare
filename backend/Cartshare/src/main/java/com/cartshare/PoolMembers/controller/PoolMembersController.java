package com.cartshare.PoolMembers.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cartshare.PoolMembers.dao.PoolMembersDAO;
import com.cartshare.models.PoolMembers;
import com.cartshare.utils.MailController;

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
			// System.out.println(pool_id + " " + member_id + " " + reference_id);
			PoolMembers poolMembers = poolMembersDAO.joinRequest(pool_id, member_id, reference_id);
			
			if(poolMembers == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Details");
			}

			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PutMapping(value = "/manageRequest", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> acceptRequest(@RequestParam(required = true) String poolId,
			@RequestParam(required = true) String poolMemberId,
			@RequestParam(required = true) String requestId,
			@RequestParam(required = true) String status){
		try {
			if(poolId == null || poolMemberId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}
			status = status.trim();
			if(status.compareTo("Accepted") != 0 && status.compareTo("Rejected") != 0 && status.compareTo("Approved") != 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
			}
			poolId = poolId.trim();
			poolMemberId = poolMemberId.trim();
			requestId = requestId.trim();

			Long pool_id = Long.parseLong(poolId);
			Long poolMember = Long.parseLong(poolMemberId);
			Long request = Long.parseLong(requestId);
			
			PoolMembers poolMembers = poolMembersDAO.manageRequest(pool_id, poolMember, status, request);
			if(poolMembers == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Details");
			}
			System.out.println(poolMembers);
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
			
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping(value="/supportedRequests", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> supportedRequests(@RequestParam(required = true) String referenceId){
		try {
			if(referenceId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}
			
			referenceId = referenceId.trim();
			
			Long reference = Long.parseLong(referenceId);
			
			List<PoolMembers> poolMembers = poolMembersDAO.supportedRequests(reference);
			if(poolMembers.size() == 0)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Supported Requests");
			
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
				
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	@GetMapping(value="/requestedRequests", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> requestedRequests(@RequestParam(required = true) String screenName, 
			@RequestParam(required = true) String isLeader){
		try {
			if(screenName == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			
			screenName = screenName.trim();
			boolean isLeaderFlag = false;
			
			if(isLeader.equalsIgnoreCase("true")) {
				isLeaderFlag = true;
			}
			List<PoolMembers> poolMembers = poolMembersDAO.requestedRequests(screenName, isLeaderFlag);
			if(poolMembers.size() == 0)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Requested Requests");
			
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping(value="/viewPoolMembers", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> viewPoolMembers(@RequestParam(required = true) String poolId){
		try {
			if(poolId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}
			poolId = poolId.trim();
			
			Long pool = Long.parseLong(poolId);
			List<PoolMembers> poolMembers = poolMembersDAO.viewPoolMembers(pool);
			if(poolMembers.size() == 0)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Requested Requests");
			
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
			
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping(value="/getPoolByUser/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getPoolByUser(@Valid @PathVariable(name = "userId") String id){
		try {
			if(id == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			
			id = id.trim();
			Long userId = Long.parseLong(id);
			PoolMembers poolMembers = poolMembersDAO.getPoolByUserId(userId);
			
			if(poolMembers == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Does not belong to any pool");
			}
			// System.out.println(poolMembers);
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
