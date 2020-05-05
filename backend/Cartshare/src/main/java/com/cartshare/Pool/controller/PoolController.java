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
import com.cartshare.utils.Alphanumeric;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/pool")
public class PoolController {
	
	@Autowired
	PoolDAO poolDAO;

	Alphanumeric alphanumeric = new Alphanumeric();
	
	@PostMapping(value = "/createPool", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createPool(@RequestParam(required = false) String description,
			@RequestParam(required=true) String neighborhoodName,
			@RequestParam(required=true) String poolId,
			@RequestParam(required=true) String poolName,
			@RequestParam(required=false) String zipcode,
			@RequestParam(required=true) String poolerId){
		try {
			poolName = poolName.trim();
			neighborhoodName = neighborhoodName.trim();
			poolId = poolId.trim();
			if (alphanumeric.isAlphaNumeric(poolId) == false) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pool ID must be alphanumeric");
			}
			if(description != null) {
				description = description.trim();
			}
			if(zipcode != null) {
				zipcode = zipcode.trim();
			}
			
			Long pooler = null;
			try {
				pooler = Long.parseLong(poolerId);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Pooler ID");
			}
			
			Pool pool = new Pool();
			pool.setPoolId(poolId);
			pool.setDescription(description);
			pool.setNeighborhoodName(neighborhoodName);
			pool.setPoolName(poolName);
			pool.setZipcode(zipcode);
			Pool result = poolDAO.createPool(pool, pooler);
			if(result == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Pooler");
			}
			return ResponseEntity.status(HttpStatus.OK).body(result);
			// return null;
			
		} catch(Exception e) {
			e.printStackTrace();
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
			@RequestParam(required=true) String poolerId){
		try {
			poolName = poolName.trim();
			neighborhoodName = neighborhoodName.trim();
			if(description != null) {
				description = description.trim();
			}
			
			Long pooler = Long.parseLong(poolerId);
			Long poolId = Long.parseLong(id);
			
			Pool pool = poolDAO.getPool(poolId);
			if(pool == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid pool ID");
			} else if (pool.getPooler().getId() != pooler) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pooler is not leader");
			}
			pool.setDescription(description);
			pool.setNeighborhoodName(neighborhoodName);
			pool.setPoolName(poolName);
			poolDAO.save(pool);
			
			return ResponseEntity.status(HttpStatus.OK).body(pool);
		} catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
}
