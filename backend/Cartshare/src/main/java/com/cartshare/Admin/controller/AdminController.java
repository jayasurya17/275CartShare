package com.cartshare.Admin.controller;

import java.util.*;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.Product.dao.ProductDAO;
import com.cartshare.RequestBody.StoreRequest;
import com.cartshare.RequestBody.ProductRequest;
import com.cartshare.models.*;
import com.cartshare.s3.s3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    @Autowired
    s3Service s3;

    @PostMapping(value = "/create/store", produces = { "application/json", "application/xml" } )
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
            
            User user = userDAO.findById(userId);
            if (user == null || user.isAdmin() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not an admin ID");
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

    @PostMapping(value = "/add/products", produces = { "application/json", "application/xml" },consumes = { "multipart/form-data" })
    public ResponseEntity<?> addProducts(@RequestParam("productImage") MultipartFile myfile,@RequestParam("userId") Long userid,@RequestParam("storeIDs") ArrayList<String> storeids,@RequestParam("productName") String productname,@RequestParam("productDescription") String productdescription,
    		@RequestParam("productBrand") String productbrand,@RequestParam("productUnit") String productunit,@RequestParam("price") String price1,@RequestParam("sku") String sku) {
    	
    	
    	String productPhoto="https://toppng.com/uploads/preview/clipart-free-seaweed-clipart-draw-food-placeholder-11562968708qhzooxrjly.png";
        try {
        	try {
         productPhoto=s3.uploadFile(myfile);
        		
        	}
        	 catch (Exception e) {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product image upload failed");
             }
        	

            Long userId = null;
            try {
                userId = userid;
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null || user.isAdmin() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not an admin ID");
            }
            Double price = null;
            try {
                price = Double.parseDouble(price1);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid price");
            }

            Long SKU = null;
            try {
                SKU = Long.parseLong(sku);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product SKU");
            }

            for (String id : storeids) {
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
                Product product = productDAO.findByStoreIdAndSKU(store, SKU);
                if (product != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate SKU");
                }
            }

            List<Product> createdProducts = new ArrayList<>();
            for (String id : storeids) {
                Product product = new Product();
                // Product createdProduct;
                Long reqStoreId = null;
                try{
                    reqStoreId = Long.parseLong(id);
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
                }
                product.setStore(storeDAO.findById(reqStoreId));
                product.setSku(SKU);
                product.setProductName(productname);
                product.setDescription(productdescription);
                product.setImageURL(productPhoto);
                product.setBrand(productbrand);
                product.setUnit(productunit);
                product.setPrice(price);
                createdProducts.add(productDAO.save(product));
            }

            return ResponseEntity.status(HttpStatus.OK).body(createdProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}