package com.cartshare.Orders.controller;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.*;
import java.util.*;
import com.cartshare.RequestBody.*;
import com.cartshare.Orders.dao.OrdersDAO;
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Long productId = null;
            try {
                productId = Long.parseLong(orderRequest.getProductId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID");
            }
            Product product = productDAO.findById(productId);
            if (product.getStore().getId() != storeId) {
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
            List<Orders> allOrdersByUser = ordersDAO.findOrdersByUser(user);
            Orders order = null;
            for (Orders orderObj : allOrdersByUser) {
                if (orderObj.getStatus().equals("Cart")) {
                    if (orderObj.getStore().getId() != storeId) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("Cart cannot have products from multiple stores");
                    }
                    order = orderObj;
                }
            }

            if (order == null) {
                // Create new orderObj with pool

            }

            List<OrderItems> itemsPresentInOrder = ordersDAO.findProductsInAOrder(order);
            Product productObjInOrder;
            for (OrderItems orderItems : itemsPresentInOrder) {
                productObjInOrder = orderItems.getProduct();
                if (productObjInOrder.getId() == product.getId()) {
                    orderItems.setQuantity(orderItems.getQuantity() + quantity);
                    ordersDAO.saveOrderItem(orderItems);
                    return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.saveOrderItem(orderItems));
                }
            }

            OrderItems orderItem = new OrderItems(order, product, quantity);
            return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.saveOrderItem(orderItem));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/activeStoreInCart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getactiveStoreInCart(@RequestParam(value = "UserId") String reqUserId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            List<Orders> allOrdersByUser = ordersDAO.findOrdersByUser(user);
            Orders order = null;
            for (Orders orderObj : allOrdersByUser) {
                if (orderObj.getStatus().equals("Cart")) {
                    order = orderObj;
                }
            }
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order");
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
            Long productId = null;
            try {
                productId = Long.parseLong(orderRequest.getProductId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID");
            }
            Product product = productDAO.findById(productId);
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
            List<Orders> allOrdersByUser = ordersDAO.findOrdersByUser(user);
            Orders order = null;
            for (Orders orderObj : allOrdersByUser) {
                if (orderObj.getStatus().equals("Cart")) {
                    order = orderObj;
                }
            }
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order");
            }

            List<OrderItems> itemsPresentInOrder = ordersDAO.findProductsInAOrder(order);
            Product productObjInOrder;
            for (OrderItems orderItems : itemsPresentInOrder) {
                productObjInOrder = orderItems.getProduct();
                if (productObjInOrder.getId() == product.getId()) {
                    orderItems.setQuantity(quantity);
                    ordersDAO.saveOrderItem(orderItems);
                    return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.saveOrderItem(orderItems));
                }
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist in order");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/removeProductFromCart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> removeProductFromCart(@RequestParam(value = "userId") String reqUserId,
            @RequestParam(value = "productId") String reqProductId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Long productId = null;
            try {
                productId = Long.parseLong(reqProductId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID");
            }
            Product product = productDAO.findById(productId);

            User user = userDAO.findById(userId);
            List<Orders> allOrdersByUser = ordersDAO.findOrdersByUser(user);
            Orders order = null;
            for (Orders orderObj : allOrdersByUser) {
                if (orderObj.getStatus().equals("Cart")) {
                    order = orderObj;
                }
            }
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order");
            }

            List<OrderItems> itemsPresentInOrder = ordersDAO.findProductsInAOrder(order);
            Product productObjInOrder;
            for (OrderItems orderItems : itemsPresentInOrder) {
                productObjInOrder = orderItems.getProduct();
                if (productObjInOrder.getId() == product.getId()) {
                    ordersDAO.removeProductFromCart(productObjInOrder.getId());

                    itemsPresentInOrder = ordersDAO.findProductsInAOrder(order);
                    if (itemsPresentInOrder.size() == 0) {
                        ordersDAO.removeOrderFromCart(order.getId());
                    }

                    return ResponseEntity.status(HttpStatus.OK).body("Product removed from cart");
                }
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist in order");

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
            List<Orders> allOrdersByUser = ordersDAO.findOrdersByUser(user);
            Orders order = null;
            for (Orders orderObj : allOrdersByUser) {
                if (orderObj.getStatus().equals("Cart")) {
                    order = orderObj;
                }
            }
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order");
            }
            
            if (orderRequest.getSelfPickup()) {
                order.setPickupPooler(user);
                user.setContributionCredit(user.getContributionCredit() + 1);
                userDAO.save(user);
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