package com.cartshare.Orders.controller;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.*;
import java.util.*;
import com.cartshare.RequestBody.*;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.Orders.dao.OrdersDAO;
import com.cartshare.Pool.dao.PoolDAO;
import com.cartshare.Product.dao.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    OrdersDAO ordersDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    PoolDAO poolDAO;

    @PostMapping(value = "/add/cart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> addProductToCart(@RequestBody OrderRequest orderRequest) {

        try {
            Long userId = null;
            try {
                userId = Long.parseLong(orderRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Long storeId = null;
            try {
                storeId = Long.parseLong(orderRequest.getStoreId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            Long productId = null;
            try {
                productId = Long.parseLong(orderRequest.getProductId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID");
            }
            Product product = productDAO.findById(productId);
            if (product == null || product.getStore().getId() != storeId) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not belong to the store");
            }
            Long quantity = null;
            try {
                quantity = Long.parseLong(orderRequest.getQuantity());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }
            if (quantity < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                order = new Orders();
                order.setUser(user);

                // Update with pool the pooler is part of
                order.setPool(poolDAO.getPool(Long.parseLong("34")));
                order.setStore(storeDAO.findById(storeId));
                order.setStatus("Cart");
                order = ordersDAO.saveOrderDetails(order);
            } else if (order.getStore().getId() != storeId) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Cart cannot have products from multiple stores");
            }
            
            OrderItems orderItem = ordersDAO.findOrderItemsByOrdersAndProduct(order, product); 
            if (orderItem == null) {
                orderItem = new OrderItems(order, product, quantity);
            } else {
                orderItem.setQuantity(orderItem.getQuantity() + quantity);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.saveOrderItem(orderItem));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/activeStoreInCart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getactiveStoreInCart(@RequestParam(value = "userId") String reqUserId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            return ResponseEntity.status(HttpStatus.OK).body(order.getStore());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/update/cart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> updateQuantityOfProductInCart(@RequestBody OrderRequest orderRequest) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(orderRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Long quantity = null;
            try {
                quantity = Long.parseLong(orderRequest.getQuantity());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }
            if (quantity < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }
            Long orderItemId = null;
            try {
                orderItemId = Long.parseLong(orderRequest.getOrderItemId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order item ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            OrderItems orderItem = ordersDAO.findOrderItemsById(orderItemId);
            if (orderItem == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist in order");
            }
            orderItem.setQuantity(quantity);
            return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.saveOrderItem(orderItem));            

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/removeProductFromCart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> removeProductFromCart(@RequestParam(value = "userId") String reqUserId,
            @RequestParam(value = "orderItemId") String reqOrderItemId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            
            Long orderItemId = null;
            try {
                orderItemId = Long.parseLong(reqOrderItemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order item ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            OrderItems orderItem = ordersDAO.findOrderItemsById(orderItemId);
            if (orderItem == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist in order");
            }
            ordersDAO.removeProductFromCart(orderItemId);
            List<OrderItems> itemsPresentInOrder = ordersDAO.findProductsInAOrder(order);
            if (itemsPresentInOrder.size() == 0) {
                ordersDAO.removeOrderFromCart(order.getId());
            }

            return ResponseEntity.status(HttpStatus.OK).body("Product removed from cart");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/confirmOrder", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> confirmOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(orderRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            for (OrderItems orderItem: order.getOrderItems()) {
                orderItem.setProductName(orderItem.getProduct().getProductName());
                orderItem.setProductPrice(orderItem.getProduct().getPrice());
                orderItem.setProductImage(orderItem.getProduct().getImageURL());
                ordersDAO.saveOrderItem(orderItem);
            }
            
            if (orderRequest.getSelfPickup() == true) {
                order.setPickupPooler(user);
            }
            order.setStatus("Confirmed");
            order = ordersDAO.saveOrderDetails(order);
            
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}