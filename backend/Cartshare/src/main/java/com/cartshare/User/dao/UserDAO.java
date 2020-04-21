package com.cartshare.User.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartshare.models.User;
import com.cartshare.repositories.UserRepository;

@Service
public class UserDAO {

	@Autowired
	UserRepository userRepository;

	public User save(User player) {
		return userRepository.save(player);
	}

	public boolean nickNameExists(String nickName) {
		return userRepository.existsByNickName(nickName);
	}

	public boolean screenNameExists(String screenName) {
		return userRepository.existsByScreenName(screenName);
	}

	public User findById(String id) {
		Long userId = Long.parseLong(id);
		return userRepository.findById(userId).orElse(null);
	}

}