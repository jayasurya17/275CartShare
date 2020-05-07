package com.cartshare.Product.controller;

import java.util.*;
import javax.validation.Valid;
import com.cartshare.Product.dao.ProductDAO;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    StoreDAO storeDAO;

    @GetMapping(value = "/get/all", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getAllProducts(@Valid @RequestParam(name = "storeId", required = false) String storeId) {

        try {
            if (storeId == null) {
                return ResponseEntity.status(HttpStatus.OK).body(productDAO.findAllByIsActive());
            }
            Long reqStoreId = null;
            try{
                reqStoreId = Long.parseLong(storeId);
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            Store store = storeDAO.findById(reqStoreId);
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            if (store.isActive() == false) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Store has been deleted");
            }

            return ResponseEntity.status(HttpStatus.OK).body(productDAO.findByStoreAndIsActive(store));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping(value = "/get/details", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getProductDetails(@Valid @RequestParam(name = "productId") String productId) {

        try {
            Long reqProductId = null;
            try{
                reqProductId = Long.parseLong(productId);
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }

            Product product = productDAO.findById(reqProductId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist");
            } else if (product.isActive() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product has been deleted");
            }
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping(value = "/search/all", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> searchAllProducts(@Valid @RequestParam(name = "storeId", required = false) String storeId,
            @RequestParam(name = "SKU", required = false) String SKU,
            @RequestParam(name = "name", required = false) String name) {

        try {
            Store store;
            List<Product> allProducts = new ArrayList<>();
            List<Product> filteredProducts = new ArrayList<>();
            if (storeId != null) {
                Long reqStoreId = null;
                try{
                    reqStoreId = Long.parseLong(storeId);
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
                }
                store = storeDAO.findById(reqStoreId);
                if (store == null) {
                    return ResponseEntity.status(HttpStatus.OK).body(filteredProducts);
                }
                allProducts = productDAO.findByStore(store);
                if (SKU != null) {
                    Long reqSku;
                    try {
                        reqSku = Long.parseLong(SKU);
                    } catch(Exception e) {
                        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
                    }
                    List<Product> tempList = new ArrayList<>();
                    for (Product temp: allProducts) {
                        if (temp.getSku() == reqSku) {
                            tempList.add(temp);
                        }
                    }
                    allProducts = tempList;
                }
            } else if (SKU != null) {
                Long reqSku;
                try {
                    reqSku = Long.parseLong(SKU);
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid SKU");
                }
                allProducts = productDAO.findBySKU(reqSku);
            } else {
                allProducts = productDAO.findAll();
            }

            if (name == null || name.trim().isEmpty()) {
                filteredProducts = allProducts;
            } else {
                for (Product product : allProducts) {
                    if (product.getProductName().equals(name) && product.isActive() == true) {
                        filteredProducts.add(product);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(filteredProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/search/byAdmin", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> searchProductsByAdmin(@Valid @RequestParam(name = "userId") String userId,
            @RequestParam(name = "storeId", required = false) String storeId,
            @RequestParam(name = "SKU", required = false) String SKU,
            @RequestParam(name = "name", required = false) String name) {

        try {
            Long adminId;
            try {
                adminId = Long.parseLong(userId);
                Long.parseLong(storeId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID");
            }

            Store store;
            List<Product> allProducts = new ArrayList<>();
            List<Product> filteredProducts = new ArrayList<>();
            if (storeId != null) {
                Long reqStoreId = null;
                try{
                    reqStoreId = Long.parseLong(storeId);
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
                }
                store = storeDAO.findById(reqStoreId);
                if (store == null) {
                    return ResponseEntity.status(HttpStatus.OK).body(filteredProducts);
                } else if (store.getUser().getId() != adminId) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin does not own the store");
                }
                allProducts = productDAO.findByStore(store);
            } else if (SKU != null) {
                Long reqSku;
                try {
                    reqSku = Long.parseLong(SKU);
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid SKU");
                }
                allProducts = productDAO.findBySKU(reqSku);
            } else {
                allProducts = productDAO.findAll();
            }

            if (name == null || name.trim().isEmpty()) {
                for (Product product : allProducts) {
                    if (product.getStore().getUser().getId() == adminId) {
                        filteredProducts.add(product);
                    }
                }
            } else {
                for (Product product : allProducts) {
                    if (product.getProductName().equals(name) && product.getStore().getUser().getId() == adminId && product.isActive() == true){
                        filteredProducts.add(product);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(filteredProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}