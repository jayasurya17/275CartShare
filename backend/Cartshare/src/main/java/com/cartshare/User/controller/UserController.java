package com.cartshare.User.controller;

import java.util.Random;

import javax.validation.Valid;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.User;
import com.cartshare.repositories.UserRepository;
import com.cartshare.utils.MailController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        User user = new User(uid, email, nickName, screenName, isadmin, isverified, isactive, isprofilecomplete, "0000", null, null, null, null);
        
        MailController mc = new MailController();

        Random random = new Random();
        String code = String.format("%04d", random.nextInt(10000));
        String message = "Your 4-digit verification code is: " + code + "\n";
        boolean s = mc.send(email, "CartShare Verification Email", message);
        if(s){
            user.setVerificationCode(code);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was a problem while sending the verification email");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userDAO.save(user));

    }

    @PostMapping(value="/{id}/sendVerification", produces = { "application/json", "application/xml" })
    public ResponseEntity sendVerificationEmail(@Valid
                                    @PathVariable(name = "id") String id){
                                    // @RequestParam(name = "email") String email){
        
        Long l;
        try{
            l = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid player ID");
        }
        User user = userDAO.findById(l);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
        }
        MailController mc = new MailController();

        Random random = new Random();
        String code = String.format("%04d", random.nextInt(10000));
        String email = user.getEmail();
        String message = "Your 4-digit verification code is: " + code + "\n";
        boolean s = mc.send(email, "CartShare Verification Email", message);
        if(s){
            user.setVerificationCode(code);
            userDAO.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(code);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was a problem while sending the email");
        }

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

    @GetMapping(value="/uid/{uid}", produces = { "application/json", "application/xml" })
    public ResponseEntity getUID(@Valid @PathVariable(name = "uid") String uid){
        try{
            User user = userDAO.findByUid(uid);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user UID");
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


    @DeleteMapping(value="/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity deleteUser(@Valid @PathVariable(name = "id") String id){
        try{
            Long l;
            try{
	            l = Long.parseLong(id);
	        } catch (NumberFormatException e) {
	        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid player ID");
            }
            User user = userDAO.findById(l);
            if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with the given ID does not exist");
            }
            userDAO.deleteUser(l);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }

}