package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartshare.models.PoolMembers;
import com.cartshare.models.User;

public interface PoolMembersRepository extends JpaRepository<PoolMembers, Long>{
    public PoolMembers findByMember(User member);
}
