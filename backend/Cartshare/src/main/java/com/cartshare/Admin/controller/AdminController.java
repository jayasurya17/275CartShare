package com.cartshare.Admin.controller;

import java.util.*;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.Admin.dao.AdminDAO;
import com.cartshare.Orders.dao.OrdersDAO;
import com.cartshare.Product.dao.ProductDAO;
import com.cartshare.RequestBody.StoreRequest;
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
    AdminDAO adminDAO;

    @Autowired
    OrdersDAO ordersDAO;

    @Autowired
    s3Service s3;

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
            if (adminDAO.findByName(storeRequest.getStoreName()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate store name");
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
            try {
                reqStoreId = Long.parseLong(storeRequest.getStoreId());
            } catch (Exception e) {
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

    @PostMapping(value = "/add/products", produces = { "application/json", "application/xml" }, consumes = {
            "multipart/form-data" })
    public ResponseEntity<?> addProducts(@RequestParam(name = "productImage", required = false) MultipartFile myfile,
            @RequestParam("userId") Long userid, @RequestParam("storeIDs") ArrayList<String> storeids,
            @RequestParam("productName") String productname,
            @RequestParam("productDescription") String productdescription,
            @RequestParam("productBrand") String productbrand, @RequestParam("productUnit") String productunit,
            @RequestParam("price") String price1, @RequestParam("sku") String sku) {

        String productPhoto = "https://toppng.com/uploads/preview/clipart-free-seaweed-clipart-draw-food-placeholder-11562968708qhzooxrjly.png";
        try {
            try {
                productPhoto = s3.uploadFile(myfile);

            } catch (Exception e) {
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
                try {
                    reqStoreId = Long.parseLong(id);
                } catch (Exception e) {
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
                try {
                    reqStoreId = Long.parseLong(id);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
                }
                product.setStore(storeDAO.findById(reqStoreId));
                product.setSku(SKU);
                product.setProductName(productname);
                product.setDescription(productdescription);
                product.setBrand(productbrand);
                product.setUnit(productunit);
                product.setPrice(price);
                if (myfile != null) {
                    product.setImageURL(productPhoto);
                } else {
                    product.setImageURL("https://toppng.com/uploads/preview/clipart-free-seaweed-clipart-draw-food-placeholder-11562968708qhzooxrjly.png");
                }
                createdProducts.add(productDAO.save(product));
            }

            return ResponseEntity.status(HttpStatus.OK).body(createdProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/update/product", produces = { "application/json", "application/xml" }, consumes = {
            "multipart/form-data" })
    public ResponseEntity<?> updateProducts(@RequestParam(name = "productImage", required = false) MultipartFile myfile,
            @RequestParam("userId") Long userid, @RequestParam("productId") String reqProductId,
            @RequestParam("productName") String productname,
            @RequestParam("productDescription") String productdescription,
            @RequestParam("productBrand") String productbrand, @RequestParam("productUnit") String productunit,
            @RequestParam("productPrice") String productPrice) {

        String productPhoto = "https://toppng.com/uploads/preview/clipart-free-seaweed-clipart-draw-food-placeholder-11562968708qhzooxrjly.png";
        try {
            try {
                productPhoto = s3.uploadFile(myfile);
            } catch (Exception e) {
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
                price = Double.parseDouble(productPrice);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid price");
            }
            Long productId = null;
            try {
                productId = Long.parseLong(reqProductId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product details");
            }

            Product product = productDAO.findById(productId);
            product.setProductName(productname);
            product.setDescription(productdescription);
            product.setBrand(productbrand);
            product.setUnit(productunit);
            product.setPrice(price);
            if (myfile != null) {
                product.setImageURL(productPhoto);
            }
            product = productDAO.save(product);

            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/store", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> deleteStore(@RequestParam("storeId") String reqStoreId) {

        try {
            
            Long storeId = null;
            try {
                storeId = Long.parseLong(reqStoreId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }

            Store store = storeDAO.findById(storeId);
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store with the given ID does not exist");
            }
            
            List<Orders> allOrders = ordersDAO.findAllUnfulfilledOrdersByStore(store);

            for (Orders order: allOrders) {
                if (order.getStatus().equals("Cart")) {
                    for (OrderItems orderItems: order.getOrderItems()) {
                        adminDAO.deleteOrderItem(orderItems.getId());
                    }
                    adminDAO.deleteOrder(order.getId());
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("There are unfulfilled orders in this store");
                }
            }

            store.setActive(false);
            storeDAO.save(store);

            return ResponseEntity.status(HttpStatus.OK).body(store);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @DeleteMapping(value = "/delete/product", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") String reqProductId) {

        try {
            
            Long productId = null;
            try {
                productId = Long.parseLong(reqProductId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID");
            }

            Product product = productDAO.findById(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product with the given ID does not exist");
            }
            
            List<Orders> allOrders = ordersDAO.findAllUnfulfilledOrdersByStore(product.getStore());

            for (Orders order: allOrders) {
                if (order.getStatus().equals("Cart")) {
                    for (OrderItems orderItems: order.getOrderItems()) {
                        if (orderItems.getProduct().getId() == productId) {
                            adminDAO.deleteOrderItem(orderItems.getId());
                        }
                    }
                    if (order.getOrderItems().isEmpty()) {
                        adminDAO.deleteOrder(order.getId());
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("There are unfulfilled orders in this store");
                }
            }

            product.setActive(false);
            productDAO.save(product);

            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}