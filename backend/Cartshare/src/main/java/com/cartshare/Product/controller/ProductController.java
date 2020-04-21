package com.cartshare.Product.controller;

import javax.validation.Valid;
import com.cartshare.Product.dao.ProductDAO;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.*;
import com.cartshare.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductDAO productDAO ;

    @Autowired
    UserDAO userDAO ;

    @Autowired
    StoreDAO storeDAO;

    @GetMapping(value = "/get/all", produces = { "application/json", "application/xml" })
    public ResponseEntity createUser(@Valid
                                    @RequestParam(name = "userId") String userId,
                                    @RequestParam(name = "storeId") String storeId){

        try {
            Store store = storeDAO.findById(storeId);
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            } else if (user.isAdmin() && store.getUser().getId() != user.getId()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin does not own the store");
            }

            return ResponseEntity.status(HttpStatus.OK).body(productDAO.findByStore(store));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        

    }

}