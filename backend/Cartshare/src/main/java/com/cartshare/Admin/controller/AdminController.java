package com.cartshare.Admin.controller;

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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    UserDAO userDAO;

    @PostMapping(value = "/create/store", produces = { "application/json", "application/xml" })
    public ResponseEntity createStore(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "storeName") String storeName, 
            @RequestParam(name = "street") String street,
            @RequestParam(name = "city") String city, 
            @RequestParam(name = "state") String state,
            @RequestParam(name = "zipcode") String zipcode) {

        try {
            Address address = new Address();
            address.setStreet(street);
            address.setCity(city);
            address.setState(state);
            address.setZipcode(zipcode);
            User user = userDAO.findById(userId);
            Store store = new Store();
            store.setStoreName(storeName);
            store.setActive(true);
            store.setAddress(address);
            store.setUser(user);

            return ResponseEntity.status(HttpStatus.OK).body(storeDAO.save(store));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/modify/store", produces = { "application/json", "application/xml" })
    public ResponseEntity modifyStore(
                @RequestParam(name = "userId") String userId,
                @RequestParam(name = "storeId") String storeId,
                @RequestParam(name = "storeName") String storeName, 
                @RequestParam(name = "street") String street,
                @RequestParam(name = "city") String city, 
                @RequestParam(name = "state") String state,
                @RequestParam(name = "zipcode") String zipcode) {
            
            try {
                Store store = storeDAO.findById(storeId);
                if (store == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store with the given ID does not exist");
                }
                if (store.getUser().getId() != Long.parseLong(userId)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin does not own the store");
                }
                store.setStoreName(storeName);
                Address address = new Address();
                address.setStreet(street);
                address.setCity(city);
                address.setState(state);
                address.setZipcode(zipcode);
                store.setAddress(address);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(storeDAO.save(store));
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }

        }
}