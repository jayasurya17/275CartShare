package com.cartshare.Pool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pool")
public class PoolController {
	
	
	@PostMapping(value = "/createPool", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createPool(@RequestBody(required = false) String description,
			@RequestBody(required=false) String neighborhoodName,
			@RequestBody(required=false) String poolName,
			@RequestBody(required=false) String zipcode,
			@RequestBody(required=true) String leaderId,
			@RequestBody(required=true) String poolerId){
		
		System.out.println(description);
		System.out.println(neighborhoodName);
		System.out.println(poolName);
		System.out.println(zipcode);
		System.out.println(leaderId);
		System.out.println(poolerId);
		
		return ResponseEntity.status(HttpStatus.OK).body("Hello");
		// return null;
		
	}
}
