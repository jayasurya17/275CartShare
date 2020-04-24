package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartshare.models.PoolMembers;

public interface PoolMembersRepository extends JpaRepository<PoolMembers, Long>{

}
