package com.cartshare.PoolMembers.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cartshare.PoolMembers.controller.PoolMembersDAO;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/pool")
public class PoolMembersController {
	
	@Autowired
	PoolMembersDAO poolMembersDAO;
	
	@PostMapping(value = "/joinPool", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> joinPool(@RequestParam(required = true) String poolId,
			@RequestParam(required = true) String userId){
		return null;
	}
}
