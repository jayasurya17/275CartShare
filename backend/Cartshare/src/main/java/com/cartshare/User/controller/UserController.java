package com.cartshare.User.controller;

import javax.validation.Valid;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.User;
import com.cartshare.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import antlr.ASdebug.ASDebugStream;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDAO userDAO;

    @PostMapping(produces = { "application/json", "application/xml" })
    public ResponseEntity createUser(@Valid
                                    @RequestParam(name = "uid") String uid,
                                    @RequestParam(name = "email") String email,
                                    @RequestParam(name = "nickName") String nickName,
                                    @RequestParam(name = "screenName") String screenName,
                                    @RequestParam(name = "isAdmin") String isAdmin,
                                    @RequestParam(name = "isVerified") String isVerified,
                                    @RequestParam(name = "isActive") String isActive,
                                    @RequestParam(name = "isProfileComplete") String isProfileComplete){

        if(userDAO.nickNameExists(nickName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nick Name already exists");
        }
        if(userDAO.screenNameExists(screenName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Screen Name already exists");
        }
        boolean isadmin = (isAdmin.compareTo("true") == 0) ? true : false;
        boolean isverified = (isVerified.compareTo("true") == 0) ? true : false;
        boolean isactive = (isActive.compareTo("true") == 0) ? true : false;
        boolean isprofilecomplete = (isProfileComplete.compareTo("true") == 0) ? true : false;
        User user = new User(uid, email, nickName, screenName, isadmin, isverified, isactive, isprofilecomplete, null, null, null, null, null);
        
        return ResponseEntity.status(HttpStatus.OK).body(userDAO.save(user));

    }

    @GetMapping(value="/{id}", produces = { "application/json", "application/xml" })
	public ResponseEntity findUser(@Valid @PathVariable(name = "id") String id) {
        try{
            Long l ;
            try{
                l = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            User user = userDAO.findById(l);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

    }


    @PutMapping(value="/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity editUser(@Valid
                                    @PathVariable(name = "id") String id,
                                    @RequestParam(name = "email") String email,
                                    @RequestParam(name = "nickName") String nickName,
                                    @RequestParam(name = "screenName") String screenName,
                                    @RequestParam(name = "isAdmin") String isAdmin,
                                    @RequestParam(name = "isVerified") String isVerified,
                                    @RequestParam(name = "isActive") String isActive,
                                    @RequestParam(name = "isProfileComplete") String isProfileComplete){

        try {
            Long l ;
            try{
                l = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid player ID");
            }
            User user = userDAO.findById(l);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID doesn't exist");
            }
            if(userDAO.isDuplicateNickName(nickName, l)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nick Name already exists. Please chose a unique Nick Name");
            }
            if(userDAO.isDuplicateScreenName(screenName, l)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Screen Name already exists. Please chose a unique Screen Name");
            }
            boolean isadmin = (isAdmin.compareTo("true") == 0) ? true : false;
            boolean isverified = (isVerified.compareTo("true") == 0) ? true : false;
            boolean isactive = (isActive.compareTo("true") == 0) ? true : false;
            boolean isprofilecomplete = (isProfileComplete.compareTo("true") == 0) ? true : false;
            user.setEmail(email);
            user.setNickName(nickName);
            user.setScreenName(screenName);
            user.setAdmin(isadmin);
            user.setVerified(isverified);
            user.setActive(isactive);
            user.setProfileComplete(isprofilecomplete);
            return ResponseEntity.status(HttpStatus.OK).body(userDAO.save(user));


        } catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }

}