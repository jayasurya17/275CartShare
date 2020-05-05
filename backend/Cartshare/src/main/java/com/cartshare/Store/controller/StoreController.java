package com.cartshare.Store.controller;

import javax.validation.Valid;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    UserDAO userDAO;

    @GetMapping(value = "/all",  produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getAllStores() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(storeDAO.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/details/{id}",  produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getStoreDetails(@Valid @PathVariable(name = "id") String id) {
        try{
            Long reqStoreId = null;
            try{
                reqStoreId = Long.parseLong(id);
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            Store store = storeDAO.findById(reqStoreId);
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            return ResponseEntity.status(HttpStatus.OK).body(store);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}