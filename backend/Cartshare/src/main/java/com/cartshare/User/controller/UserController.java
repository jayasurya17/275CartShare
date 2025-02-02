package com.cartshare.User.controller;

import java.util.Random;

import javax.validation.Valid;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.Address;
import com.cartshare.models.User;
import com.cartshare.utils.MailController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MailController mc;

    @PostMapping(produces = { "application/json", "application/xml" })
    public ResponseEntity<?> createUser(@Valid
                                    @RequestParam(name = "uid") String uid,
                                    @RequestParam(name = "email") String email,
                                    @RequestParam(name = "nickName") String nickName,
                                    @RequestParam(name = "screenName") String screenName,
                                    @RequestParam(name = "isAdmin") String isAdmin,
                                    @RequestParam(name = "isVerified") String isVerified,
                                    @RequestParam(name = "isActive") String isActive,
                                    @RequestParam(name = "isProfileComplete") String isProfileComplete){

        if(userDAO.nickNameExists(nickName) && nickName.compareTo("notSet") != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nick Name already exists");
        }
        if(userDAO.screenNameExists(screenName) && screenName.compareTo("notSet") != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Screen Name already exists");
        }
        boolean isadmin = (isAdmin.compareTo("true") == 0) ? true : false;
        boolean isverified = (isVerified.compareTo("true") == 0) ? true : false;
        boolean isactive = (isActive.compareTo("true") == 0) ? true : false;
        boolean isprofilecomplete = (isProfileComplete.compareTo("true") == 0) ? true : false;
        User user = new User(uid, email, nickName, screenName, isadmin, isverified, isactive, isprofilecomplete, "0000", null, null, null, null);
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
    public ResponseEntity<?> sendVerificationEmail(@Valid
                                    @PathVariable(name = "id") String id){
                                    // @RequestParam(name = "email") String email){
        
        Long l;
        try{
            l = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
        }
        User user = userDAO.findById(l);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
        }

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
	public ResponseEntity<?> findUser(@Valid @PathVariable(name = "id") String id) {
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
    public ResponseEntity<?> getUID(@Valid @PathVariable(name = "uid") String uid){
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
    public ResponseEntity<?> editUser(@Valid
                                    @PathVariable(name = "id") String id,
                                    @RequestParam(name = "email") String email,
                                    @RequestParam(name = "nickName") String nickName,
                                    @RequestParam(name = "screenName") String screenName,
                                    @RequestParam(name = "isAdmin") String isAdmin,
                                    @RequestParam(name = "isVerified") String isVerified,
                                    @RequestParam(name = "isActive") String isActive,
                                    @RequestParam(name = "isProfileComplete") String isProfileComplete,
                                    @RequestParam(name = "city") String city,
                                    @RequestParam(name = "state") String state,
                                    @RequestParam(name = "street") String street,
                                    @RequestParam(name = "zipcode") String zipcode){

        try {
            Long l ;
            try{
                l = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            User user = userDAO.findById(l);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID doesn't exist");
            }
            if(userDAO.isDuplicateNickName(nickName, l) && nickName.compareTo("notSet") != 0 && user.getNickName().compareTo(nickName) != 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nick Name already exists. Please chose a unique Nick Name");
            }
            // System.out.println("something " + user.getScreenName() + " " + screenName);
            if(userDAO.isDuplicateScreenName(screenName, l) && screenName.compareTo("notSet") != 0 && user.getScreenName().compareTo(screenName) != 0){
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

            try{
                int x = Integer.parseInt(zipcode);
            } catch(NumberFormatException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid zipcode");
            }

            Address a = new Address();
            a.setCity(city);
            a.setState(state);
            a.setStreet(street);
            a.setZipcode(zipcode);

            user.setAddress(a);

            return ResponseEntity.status(HttpStatus.OK).body(userDAO.save(user));


        } catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }


    @DeleteMapping(value="/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> deleteUser(@Valid @PathVariable(name = "id") String id){
        try{
            Long l;
            try{
	            l = Long.parseLong(id);
	        } catch (NumberFormatException e) {
	        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
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
    
    @GetMapping(value="/getUserByScreenName/{screenName}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getUserByScreenName(@Valid @PathVariable(name="screenName") String screenName){
    	try {
    		screenName = screenName.trim();
    		User user = userDAO.findByScreenName(screenName);
    		if(user == null)
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Details");
    		else
    			return ResponseEntity.status(HttpStatus.OK).body(user);
    	} catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    }

}