package com.cartshare.Pool.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.cartshare.Pool.dao.PoolDAO;
import com.cartshare.models.Pool;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/pool")
public class PoolController {
	
	@Autowired
	PoolDAO poolDAO;
	
	@PostMapping(value = "/createPool", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createPool(@RequestParam(required = false) String description,
			@RequestParam(required=true) String neighborhoodName,
			@RequestParam(required=true) String poolName,
			@RequestParam(required=false) String zipcode,
			@RequestParam(required=true) String poolerId,
			@RequestParam(required=true) String leaderId){
		try {
			
			poolName = poolName.trim();
			neighborhoodName = neighborhoodName.trim();
			if(description != null)
				description = description.trim();
			if(zipcode != null)
				zipcode = zipcode.trim();
			
			long pooler = Long.parseLong(poolerId);
			long leader = Long.parseLong(leaderId);
//			System.out.println(description);
//			System.out.println(neighborhoodName);
//			System.out.println(poolName);
//			System.out.println(zipcode);
//			System.out.println(pooler);
			
			Pool pool = new Pool();
			pool.setDescription(description);
			pool.setNeighborhoodName(neighborhoodName);
			pool.setPoolName(poolName);
			pool.setZipcode(zipcode);
			
			Pool result = poolDAO.createPool(pool, pooler, leader);
			if(result == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Pooler");
			}
			return ResponseEntity.status(HttpStatus.OK).body(result);
			// return null;
			
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
	@GetMapping(value="/viewPool/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> viewPool(@Valid @PathVariable(name = "id") String id){
		try {
			
			if(id == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Id");
			} else {
				id = id.trim();
			}
			
			Long poolId = Long.parseLong(id);
			Pool pool = poolDAO.getPool(poolId);
			if(pool == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pool Does Not Exist");
			}
			return ResponseEntity.status(HttpStatus.OK).body(pool);
		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping(value="/getAllPools", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> allPools(){
		try {
			List<Pool> pools = poolDAO.getAllPools();
			if(pools == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pools do not Exist");
			}
			return ResponseEntity.status(HttpStatus.OK).body(pools);
		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PutMapping(value="/editPool", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> editPool(@RequestParam(required=true) String id,
			@RequestParam(required = false) String description,
			@RequestParam(required=true) String neighborhoodName,
			@RequestParam(required=true) String poolName,
			@RequestParam(required=false) String zipcode,
			@RequestParam(required=true) String poolerId,
			@RequestParam(required=true) String leaderId){
		try {
			poolName = poolName.trim();
			neighborhoodName = neighborhoodName.trim();
			if(description != null)
				description = description.trim();
			if(zipcode != null)
				zipcode = zipcode.trim();
			
			long pooler = Long.parseLong(poolerId);
			long poolId = Long.parseLong(id);
			long leader = Long.parseLong(leaderId);
//			System.out.println(description);
//			System.out.println(neighborhoodName);
//			System.out.println(poolName);
//			System.out.println(zipcode);
//			System.out.println(pooler);
			
			Pool pool = new Pool();
			pool.setDescription(description);
			pool.setNeighborhoodName(neighborhoodName);
			pool.setPoolName(poolName);
			pool.setZipcode(zipcode);
			pool.setId(poolId);
			
			Pool result = poolDAO.createPool(pool, pooler, leader);
			if(result == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Pooler");
			}
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
