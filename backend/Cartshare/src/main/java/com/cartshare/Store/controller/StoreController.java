package com.cartshare.Store.controller;

import javax.validation.Valid;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    UserDAO userDAO;

    @GetMapping(value = "/all",  produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getAllStores(
        @RequestParam(name = "adminId", required = false) String adminId
    ) {
        try{
            Long l;
            try{
                l = Long.parseLong(adminId);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid admin ID");
            }
            if (adminId != null) {
                User admin = userDAO.findById(l);
                return ResponseEntity.status(HttpStatus.OK).body(storeDAO.findByAdmin(admin));
            }

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