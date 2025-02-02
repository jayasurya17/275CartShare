package com.cartshare.PoolMembers.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cartshare.Pool.dao.PoolDAO;
import com.cartshare.PoolMembers.dao.PoolMembersDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.Orders;
import com.cartshare.models.Pool;
import com.cartshare.models.PoolMembers;
import com.cartshare.models.User;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/poolMembers")
public class PoolMembersController {

	@Autowired
	PoolMembersDAO poolMembersDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	PoolDAO poolDAO;

	@PostMapping(value = "/joinPool", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> joinPool(@RequestParam(required = true) String poolId,
			@RequestParam(required = true) String userId, @RequestParam(required = true) String referenceId) {
		try {
			if (poolId == null || userId == null || referenceId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}

			poolId = poolId.trim();
			userId = userId.trim();
			referenceId = referenceId.trim();

			Long pool_id = Long.parseLong(poolId);
			Long member_id = Long.parseLong(userId);
			Long reference_id = Long.parseLong(referenceId);

			Pool pool = poolDAO.findById(pool_id);
			if (pool.getIsActive() == false) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Pool has been deleted");
			}
			PoolMembers poolMembers = poolMembersDAO.joinRequest(pool_id, member_id, reference_id);
			if (poolMembers == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Details");
			}

			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PutMapping(value = "/manageRequest", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> acceptRequest(@RequestParam(required = true) String poolId,
			@RequestParam(required = true) String poolMemberId, @RequestParam(required = true) String requestId,
			@RequestParam(required = true) String status) {
		try {
			if (poolId == null || poolMemberId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}
			status = status.trim();
			if (status.compareTo("Accepted") != 0 && status.compareTo("Rejected") != 0
					&& status.compareTo("Approved") != 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
			}
			poolId = poolId.trim();
			poolMemberId = poolMemberId.trim();
			requestId = requestId.trim();

			Long pool_id = Long.parseLong(poolId);
			Long poolMember = Long.parseLong(poolMemberId);
			Long request = Long.parseLong(requestId);

			PoolMembers poolMembers = poolMembersDAO.manageRequest(pool_id, poolMember, status, request);
			if (poolMembers == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Details");
			}
			// System.out.println(poolMembers);
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping(value = "/supportedRequests", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> supportedRequests(@RequestParam(required = true) String referenceId) {
		try {
			if (referenceId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}

			referenceId = referenceId.trim();

			Long reference = Long.parseLong(referenceId);

			List<PoolMembers> poolMembers = poolMembersDAO.supportedRequests(reference);
			if (poolMembers.size() == 0)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Supported Requests");

			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping(value = "/requestedRequests", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> requestedRequests(@RequestParam(required = true) String screenName,
			@RequestParam(required = true) String isLeader) {
		try {
			if (screenName == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");

			screenName = screenName.trim();
			boolean isLeaderFlag = false;

			if (isLeader.equalsIgnoreCase("true")) {
				isLeaderFlag = true;
			}
			List<PoolMembers> poolMembers = poolMembersDAO.requestedRequests(screenName, isLeaderFlag);
			if (poolMembers.size() == 0)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Requested Requests");

			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping(value = "/viewPoolMembers", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> viewPoolMembers(@RequestParam(required = true) String poolId) {
		try {
			if (poolId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");
			}
			poolId = poolId.trim();

			Long pool = Long.parseLong(poolId);
			List<PoolMembers> poolMembers = poolMembersDAO.viewPoolMembers(pool);
			if (poolMembers.size() == 0)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Requested Requests");

			return ResponseEntity.status(HttpStatus.OK).body(poolMembers);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping(value = "/getPoolByUser/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getPoolByUser(@Valid @PathVariable(name = "userId") String id) {
		try {
			if (id == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide required parameters");

			id = id.trim();
			Long userId = Long.parseLong(id);
			List<PoolMembers> poolMembers = poolMembersDAO.getPoolByUserId(userId);

			if (poolMembers == null || poolMembers.size() == 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Does not belong to any pool");
			}
			// System.out.println(poolMembers);
			return ResponseEntity.status(HttpStatus.OK).body(poolMembers.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@DeleteMapping(value = "/leavePool", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> leavePool(@RequestParam(value = "userId", required = true) String reqUserId,
			@RequestParam(value = "poolId", required = true) String reqPoolId) {
		try {
			Long userId = null;
			try {
				userId = Long.parseLong(reqUserId);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
			}

			User user = userDAO.findById(userId);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
			}

			Long poolId = null;
			try {
				poolId = Long.parseLong(reqPoolId);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid pool ID");
			}

			Set<Orders> orders = user.getOrders();
			for (Orders order : orders) {
				if (order.isFulfilled() == false) {
					if (order.getStatus().equals("Cart")) {
						return ResponseEntity.status(HttpStatus.CONFLICT)
								.body("Please clear your cart before leaving the pool");
					} else {
						return ResponseEntity.status(HttpStatus.CONFLICT)
								.body("You cannot leave the pool with unfulfilled orders");
					}
				}
			}

			Set<PoolMembers> member = user.getPoolMembers();
			for (PoolMembers poolMembers : member) {
				if (poolMembers.getPool().getId() == poolId) {
					poolMembersDAO.deleteById(poolMembers.getId());
				}
			}

			return ResponseEntity.status(HttpStatus.OK).body("Success");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
