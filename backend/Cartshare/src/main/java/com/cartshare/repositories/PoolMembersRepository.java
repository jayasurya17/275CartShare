package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.cartshare.models.Pool;
import com.cartshare.models.PoolMembers;
import com.cartshare.models.User;

public interface PoolMembersRepository extends JpaRepository<PoolMembers, Long>{
    public List<PoolMembers> findAllByMember(User member);
    public List<PoolMembers> findAllByPoolAndMemberAndStatus(Pool pool, User member, String status);
    public List<PoolMembers> findAllByMemberAndStatus(User member, String status);
    public PoolMembers findByMember(User member);
}
