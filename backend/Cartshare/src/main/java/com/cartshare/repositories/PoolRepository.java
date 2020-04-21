package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartshare.models.Pool;
import com.cartshare.models.User;

public interface PoolRepository extends JpaRepository<User, Long> {

	Pool save(Pool pool);

}
