package com.cartshare.Admin.controller;

import javax.validation.Valid;
import java.util.*;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.Product.dao.ProductDAO;
import com.cartshare.models.*;
import com.cartshare.RequestBody.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    ProductDAO productDAO;

    @PostMapping(value = "/create/store", produces = { "application/json", "application/xml" })
    public ResponseEntity createStore(@RequestBody StoreRequest storeRequest) {

        try {
            Address address = new Address();
            address.setStreet(storeRequest.getStreet());
            address.setCity(storeRequest.getCity());
            address.setState(storeRequest.getState());
            address.setZipcode(storeRequest.getZipcode());
            User user = userDAO.findById(storeRequest.getUserId());
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
    public ResponseEntity modifyStore(@RequestBody StoreRequest storeRequest) {

        try {
            Store store = storeDAO.findById(storeRequest.getStoreId());
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store with the given ID does not exist");
            }
            if (store.getUser().getId() != Long.parseLong(storeRequest.getUserId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin does not own the store");
            }
            store.setStoreName(storeRequest.getStoreName());
            Address address = new Address();
            address.setStreet(storeRequest.getStreet());
            address.setCity(storeRequest.getCity());
            address.setState(storeRequest.getState());
            address.setZipcode(storeRequest.getZipcode());
            store.setAddress(address);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(storeDAO.save(store));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping(value = "/add/products", produces = { "application/json", "application/xml" })
    public ResponseEntity addProducts(@RequestParam(name = "userId") String userId,
            @RequestParam(name = "storeIDs") ArrayList<String> storeIDs, @RequestParam(name = "productSKU") String productSKU,
            @RequestParam(name = "productName") String productName,
            @RequestParam(name = "productImage") String productImage,
            @RequestParam(name = "productDescription") String productDescription,
            @RequestParam(name = "productBrand") String productBrand,
            @RequestParam(name = "productUnit") String productUnit,
            @RequestParam(name = "productPrice") String productPrice) {

        try {

            User user = userDAO.findById(userId);
            if (user == null || user.isAdmin() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not an admin");
            }

            for(String id: storeIDs) {
                Store store = storeDAO.findById(id);
                if (store == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store with the given ID does not exist");
                }
                for (Product product: store.getProducts()) {
                    if (product.getSku().equalsIgnoreCase(productSKU)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate SKU");
                    }
                }
            }

            Double price;
            try {
                price = Double.parseDouble(productPrice);
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid price");
            }

            List<Product> createdProducts = new ArrayList<>();
            for(String id: storeIDs) {
                Product product = new Product();
                Product createdProduct;
                product.setStore(storeDAO.findById(id));
                product.setSku(productSKU.toUpperCase());
                product.setProductName(productName);
                product.setDescription(productDescription);
                product.setImageURL(productImage);
                product.setBrand(productBrand);
                product.setUnit(productUnit);
                product.setPrice(price);
                // return ResponseEntity.status(HttpStatus.OK).body(product);
                createdProduct = productDAO.save(product);
                createdProducts.add(createdProduct);
            }

            return ResponseEntity.status(HttpStatus.OK).body(createdProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    

    @PostMapping(value = "/update/products", produces = { "application/json", "application/xml" })
    public ResponseEntity updateProducts(@RequestParam(name = "userId") String userId,
            @RequestParam(name = "storeIDs") ArrayList<String> storeIDs, @RequestParam(name = "productSKU") String productSKU,
            @RequestParam(name = "productName") String productName,
            @RequestParam(name = "productImage") String productImage,
            @RequestParam(name = "productDescription") String productDescription,
            @RequestParam(name = "productBrand") String productBrand,
            @RequestParam(name = "productUnit") String productUnit,
            @RequestParam(name = "productPrice") String productPrice) {

        try {

            User user = userDAO.findById(userId);
            if (user == null || user.isAdmin() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not an admin");
            }

            for(String id: storeIDs) {
                Store store = storeDAO.findById(id);
                if (store == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store with the given ID does not exist");
                }
                for (Product product: store.getProducts()) {
                    if (product.getSku().equalsIgnoreCase(productSKU)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate SKU");
                    }
                }
            }

            Double price;
            try {
                price = Double.parseDouble(productPrice);
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid price");
            }

            List<Product> createdProducts = new ArrayList<>();
            for(String id: storeIDs) {
                Product product = new Product();
                Product createdProduct;
                product.setStore(storeDAO.findById(id));
                product.setSku(productSKU.toUpperCase());
                product.setProductName(productName);
                product.setDescription(productDescription);
                product.setImageURL(productImage);
                product.setBrand(productBrand);
                product.setUnit(productUnit);
                product.setPrice(price);
                // return ResponseEntity.status(HttpStatus.OK).body(product);
                createdProduct = productDAO.save(product);
                createdProducts.add(createdProduct);
            }

            return ResponseEntity.status(HttpStatus.OK).body(createdProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}