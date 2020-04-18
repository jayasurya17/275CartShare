package com.cartshare.User.controller;

import javax.validation.Valid;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import antlr.ASdebug.ASDebugStream;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        boolean isadmin = (isAdmin.compareTo("true") == 0) ? true : false;
        boolean isverified = (isVerified.compareTo("true") == 0) ? true : false;
        boolean isactive = (isActive.compareTo("true") == 0) ? true : false;
        boolean isprofilecomplete = (isProfileComplete.compareTo("true") == 0) ? true : false;
        User user = new User(uid, email, nickName, screenName, isadmin, isverified, isactive, isprofilecomplete, null, null, null, null, null);
        
        return ResponseEntity.status(HttpStatus.OK).body(userDAO.save(user));

    }

}