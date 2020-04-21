package com.cartshare.Store.controller;

import javax.validation.Valid;

import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.Address;
import com.cartshare.models.Store;
import com.cartshare.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    UserDAO userDAO;

    @GetMapping(value = "/all",  produces = { "application/json", "application/xml" })
    public ResponseEntity getAllStores(
        @RequestParam(name = "adminId", required = false) String adminId
    ) {
        try{

            if (adminId != null) {
                User admin = userDAO.findById(adminId);
                return ResponseEntity.status(HttpStatus.OK).body(storeDAO.findByAdmin(admin));
            }

            return ResponseEntity.status(HttpStatus.OK).body(storeDAO.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/details/{id}",  produces = { "application/json", "application/xml" })
    public ResponseEntity getStoreDetails(@Valid @PathVariable(name = "id") String id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(storeDAO.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}