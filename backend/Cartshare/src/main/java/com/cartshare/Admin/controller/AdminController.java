package com.cartshare.Admin.controller;

import java.util.*;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.Product.dao.ProductDAO;
import com.cartshare.RequestBody.StoreRequest;
import com.cartshare.RequestBody.ProductRequest;
import com.cartshare.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ProductDAO productDAO;

    @PostMapping(value = "/create/store", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> createStore(@RequestBody StoreRequest storeRequest) {

        try {
            Long userId = null;
            try {
                userId = Long.parseLong(storeRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Address address = new Address();
            address.setStreet(storeRequest.getStreet());
            address.setCity(storeRequest.getCity());
            address.setState(storeRequest.getState());
            address.setZipcode(storeRequest.getZipcode());
            User user = userDAO.findById(userId);
            if (user == null || user.isAdmin() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not an admin");
            }
            Store store = new Store();
            store.setStoreName(storeRequest.getStoreName());
            store.setActive(true);
            store.setAddress(address);
            store.setUser(user);

            return ResponseEntity.status(HttpStatus.OK).body(storeDAO.save(store));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/modify/store", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> modifyStore(@RequestBody StoreRequest storeRequest) {

        try {
            Long userId = null;
            try {
                userId = Long.parseLong(storeRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            
            Long reqStoreId = null;
            try{
                reqStoreId = Long.parseLong(storeRequest.getStoreId());
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }

            Store store = storeDAO.findById(reqStoreId);
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store with the given ID does not exist");
            }
            if (store.getUser().getId() != userId) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin does not own the store");
            }
            store.setStoreName(storeRequest.getStoreName());
            Address address = new Address();
            address.setStreet(storeRequest.getStreet());
            address.setCity(storeRequest.getCity());
            address.setState(storeRequest.getState());
            address.setZipcode(storeRequest.getZipcode());
            store.setAddress(address);
            return ResponseEntity.status(HttpStatus.OK).body(storeDAO.save(store));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping(value = "/add/products", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> addProducts(@RequestBody ProductRequest productRequest) {

        try {

            Long userId = null;
            try {
                userId = Long.parseLong(productRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            Double price = null;
            try {
                price = Double.parseDouble(productRequest.getProductPrice());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid price");
            }

            Long SKU = null;
            try {
                SKU = Long.parseLong(productRequest.getProductSKU());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product SKU");
            }

            User user = userDAO.findById(userId);
            if (user == null || user.isAdmin() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not an admin");
            }

            for (String id : productRequest.getStoreIDs()) {
                Long reqStoreId = null;
                try{
                    reqStoreId = Long.parseLong(id);
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
                }
                Store store = storeDAO.findById(reqStoreId);
                if (store == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store with the given ID does not exist");
                }
                for (Product product : store.getProducts()) {
                    if (product.getSku() == SKU) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate SKU");
                    }
                }
            }

            List<Product> createdProducts = new ArrayList<>();
            for (String id : productRequest.getStoreIDs()) {
                Product product = new Product();
                Product createdProduct;
                Long reqStoreId = null;
                try{
                    reqStoreId = Long.parseLong(id);
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
                }
                product.setStore(storeDAO.findById(reqStoreId));
                product.setSku(SKU);
                product.setProductName(productRequest.getProductName());
                product.setDescription(productRequest.getProductDescription());
                product.setImageURL(productRequest.getProductImage());
                product.setBrand(productRequest.getProductBrand());
                product.setUnit(productRequest.getProductUnit());
                product.setPrice(price);
                // return ResponseEntity.status(HttpStatus.OK).body(product);
                createdProduct = productDAO.save(product);
                createdProducts.add(createdProduct);
                // createdProducts.add(product);
            }

            return ResponseEntity.status(HttpStatus.OK).body(createdProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}