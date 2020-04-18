package com.cartshare.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cartshare.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public boolean existsByEmail(String email);
	
	public List<User> findByEmail(String email);


	public boolean existsByNickName(String nickName);

	public boolean existsByScreenName(String screenName);
	
}
