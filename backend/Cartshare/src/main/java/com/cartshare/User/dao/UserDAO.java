package com.cartshare.User.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.cartshare.models.User;
import com.cartshare.repositories.UserRepository;

@Service
public class UserDAO {

	@Autowired
	UserRepository userRepository;

    public User save(User user) {
      return userRepository.save(user);
    }

    public boolean userExists(Long id){
      return userRepository.existsById(id);
    }

    public boolean nickNameExists(String nickName) {
      List<User> l = userRepository.findByNickName(nickName);
      return (l.size() == 0) ? false : true;
    }

    public boolean screenNameExists(String screenName){
      List<User> l = userRepository.findByScreenName(screenName);
      return (l.size() == 0) ? false : true;
    }

    public User findByScreenName(String screenName){
      List<User> l = userRepository.findByScreenName(screenName);
      return (l.size() == 0) ? null : l.get(0);
    }

    public User findById(Long id){
      return userRepository.findById(id).orElse(null);
    }
    
    public boolean isDuplicateNickName(String nickName, Long id){
      List<User> l = userRepository.findByNickName(nickName);
      for(User u : l){
        if(u.getId() != id)
          return true;
      }
      return false;
    }

    public boolean isDuplicateScreenName(String screenName, Long id){
      List<User> l = userRepository.findByScreenName(screenName);
      for(User u : l){
        if(u.getId() != id)
          return true;
      }
      return false;
    }

    public void deleteUser(Long id){
      userRepository.deleteById(id);
    }

    public User findByUid(String uid){
      List<User> l = userRepository.findByUid(uid);
      if(l.size() == 0) return null;
      return l.get(0);
    }
}
